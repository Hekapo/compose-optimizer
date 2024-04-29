package com.github.hekapo.composeoptimizer.quickFix

import com.github.hekapo.composeoptimizer.inspections.collections.CollectionType
import com.github.hekapo.composeoptimizer.util.extractGenericTypes
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.application.runWriteAction
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFileFactory
import org.jetbrains.kotlin.idea.KotlinFileType
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.psi.KtElement
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtParameter
import org.jetbrains.kotlin.psi.KtPsiFactory
import org.jetbrains.kotlin.resolve.ImportPath

class GenerateCollectionWrapperFix(private val collectionType: CollectionType) : LocalQuickFix {

    override fun getFamilyName() = "Generate ${collectionType.kotlinType} wrapper"

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        val currentElement = descriptor.psiElement as? KtElement ?: return

        createWrapperFile(project, currentElement)

        if (currentElement is KtParameter) {
            println(currentElement.text)
            replaceWithWrapperType(currentElement)
        }
    }

    private fun createWrapperFile(project: Project, currentElement: KtElement) {
        val fileName = "${collectionType.wrapperName}.kt"
        val directory = currentElement.containingFile.containingDirectory

        // Проверка на существование файла
        if (directory.findFile(fileName) != null) return

        val fileContent = """
        package ${currentElement.containingKtFile.packageFqName.asString()}
        
        @Immutable
        @JvmInline
        value class ${collectionType.wrapperName}<${collectionType.typeArgumentsString}>(
            private val items: ${collectionType.kotlinType}<${collectionType.typeArgumentsString}>
        ) : ${collectionType.kotlinType}<${collectionType.typeArgumentsString}> by items
    """.trimIndent()

        runWriteAction {
            val psiFileFactory = PsiFileFactory.getInstance(project)
            val newFile = psiFileFactory.createFileFromText(fileName, KotlinFileType.INSTANCE, fileContent) as? KtFile
            if (newFile != null) {
                val psiFactory = KtPsiFactory(project)
                val importDirective =
                    psiFactory.createImportDirective(
                        ImportPath(
                            fqName = FqName("androidx.compose.runtime.Immutable"),
                            isAllUnder = false
                        )
                    )

                val packageDirective = newFile.packageDirective
                if (packageDirective != null) {
                    newFile.addAfter(importDirective, packageDirective)
                    newFile.addAfter(psiFactory.createNewLine(), packageDirective)
                } else {
                    newFile.add(importDirective)
                    newFile.add(psiFactory.createNewLine())
                }

                directory.add(newFile)
            }
        }

    }

    private fun replaceWithWrapperType(param: KtParameter) {
        val typeReference = param.typeReference ?: return
        val currentTypeText = typeReference.text

        val genericTypes = extractGenericTypes(typeReference) ?: return
        val genericTypesJoined = genericTypes.joinToString(", ")

        val newTypeText = when {
            currentTypeText.startsWith("List<") -> "ListWrapper<$genericTypesJoined>"
            currentTypeText.startsWith("Map<") -> "ImmutableMap<$genericTypesJoined>"
            else -> return
        }

        val psiFactory = KtPsiFactory(param.project)
        val newType = psiFactory.createType(newTypeText)

        runWriteAction {
            typeReference.replace(newType)
        }
    }
}
