/* Copyright © 2020 Yuech and/or its affiliates. All rights reserved. */
package wiki.chenxun.apt.validator;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Names;

/**
 * @author 陈勋
 * @version 1.0
 * @date 2021-06-23 3:20 下午
 */
@SupportedAnnotationTypes(value = {"me.ele.lpd.knight.clown.domain.common.jsr269.Validated"})
@SupportedSourceVersion(value = SourceVersion.RELEASE_8)
public class ValidatedAnnotationProcessor extends AbstractProcessor {

    /**
     * JavacTrees提供了待处理的抽象语法树
     * TreeMaker中了一些操作抽象语法树节点的方法
     * Names提供了创建标识符的方法
     */
    private JavacTrees trees;
    private TreeMaker treeMaker;
    private Names names;

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        this.trees = JavacTrees.instance(processingEnv);
        Context context = ((JavacProcessingEnvironment)processingEnv).getContext();
        this.treeMaker = TreeMaker.instance(context);
        this.names = Names.instance(context);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        roundEnv.getElementsAnnotatedWith(Validated.class)
            .stream().map(element -> trees.getTree(element))
            .forEach(tree -> tree.accept(new TreeTranslator() {
                @Override
                public void visitMethodDef(JCMethodDecl jcMethodDecl) {

                    // 调用 validate 方法
                    JCTree.JCExpressionStatement exec = treeMaker.Exec(
                        treeMaker.Apply(
                            com.sun.tools.javac.util.List.nil(),
                            treeMaker.Select(
                                treeMaker.Ident(
                                    names.fromString("me.ele.lpd.knight.clown.domain.common.jsr269.ValidateUtil")),
                                names.fromString("hello")
                            ),
                            com.sun.tools.javac.util.List.of(treeMaker.Literal("hello world!"))
                        ));

                    // 追加到语法树第一行
                    jcMethodDecl.getBody().getStatements().prepend(exec);

                    super.visitMethodDef(jcMethodDecl);
                }
            }));

        return true;
    }
}
