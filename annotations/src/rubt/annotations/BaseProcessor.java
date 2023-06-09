package rubt.annotations;

import java.util.Set;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;

import com.squareup.javapoet.*;

public abstract class BaseProcessor extends AbstractProcessor {

    public Messager messager;
    public Filer filer;

    /** Whether annotations have already been processed. */
    public boolean processed;

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);

        messager = env.getMessager();
        filer = env.getFiler();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_20;
    }

    public abstract void process(RoundEnvironment env) throws Exception;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        if (processed) return false; // process only once
        processed = true;

        try {
            process(env);
            return true;
        } catch (Exception ignored) {
            messager.printMessage(Kind.ERROR, ignored.getMessage());
            return false;
        }
    }

    public void write(String packageName, TypeSpec.Builder builder) throws Exception {
        JavaFile.builder(packageName, builder.build())
                .skipJavaLangImports(true)
                .addStaticImport(ClassName.get("rubt", "Vars"), "*")
                .build().writeTo(filer);
    }
}
