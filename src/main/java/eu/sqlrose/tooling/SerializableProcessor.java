package eu.sqlrose.tooling;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;

import static javax.lang.model.SourceVersion.RELEASE_6;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Jul 11, 2016
 */
@SupportedSourceVersion(RELEASE_6)
@SupportedAnnotationTypes("eu.sqlrose.annotations.Serializable")
public class SerializableProcessor extends AbstractProcessor {

    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.messager = processingEnv.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {

        for (TypeElement anno : annotations) {
            for (Element elem : env.getElementsAnnotatedWith(anno)) {
                messager.printMessage(Diagnostic.Kind.NOTE, "ADDING SERIALIZATION SUPPORT", elem);
                System.out.printf("%nADDING SERIALIZATION SUPPORT TO TYPE %s%n%n", elem.getSimpleName());
            }
        }

        return true;
    }
}
