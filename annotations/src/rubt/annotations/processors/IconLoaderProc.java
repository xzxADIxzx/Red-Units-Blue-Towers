package rubt.annotations.processors;

import arc.files.Fi;
import arc.graphics.Pixmap;
import arc.scene.style.Drawable;
import rubt.annotations.BaseProcessor;
import rubt.annotations.Annotations.IconLoader;

import javax.annotation.processing.*;
import javax.lang.model.element.*;
import javax.tools.StandardLocation;
import javax.tools.Diagnostic.Kind;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.*;

@AutoService(Processor.class)
@SupportedAnnotationTypes("rubt.annotations.Annotations.IconLoader")
public class IconLoaderProc extends BaseProcessor {

    @Override
    public void process(RoundEnvironment env) throws Exception {
        var root = TypeSpec.classBuilder("Icons").addModifiers(Modifier.PUBLIC);
        var load = MethodSpec.methodBuilder("load").addModifiers(Modifier.PUBLIC, Modifier.STATIC);

        ClassName loaderClass = ClassName.get("rubt.graphics", "Textures");
        String loaderMethod = env.getElementsAnnotatedWith(IconLoader.class).iterator().next().getSimpleName().toString();

        Fi dir = new Fi(filer.getResource(StandardLocation.CLASS_OUTPUT, "oh", "no").toUri().toURL().toString().substring(5))
                .parent().parent().parent().parent().parent().parent().child("assets/icons");

        dir.walk(icon -> {
            Pixmap pixmap = new Pixmap(icon);

            if (pixmap.getRaw(0, 0) >> 8 != -1) {
                messager.printMessage(Kind.NOTE, "icon " + icon.nameWithoutExtension() + " isn't white and will be overwritten.");
                pixmap.replace(color -> color | 0xFFFFFF00); // fill first 24 bits with 1

                icon.writePng(pixmap);
            }

            String name = icon.nameWithoutExtension().replaceAll("-", "_");

            root.addField(Drawable.class, name, Modifier.PUBLIC, Modifier.STATIC);
            load.addStatement("$L = $T.$L($S)", name, loaderClass, loaderMethod, icon.nameWithoutExtension());
        });

        write("rubt.ui", root.addMethod(load.build()));
    }
}
