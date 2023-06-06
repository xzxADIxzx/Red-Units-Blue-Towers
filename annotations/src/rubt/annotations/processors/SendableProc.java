package rubt.annotations.processors;

import arc.net.Connection;
import arc.struct.Seq;
import rubt.annotations.BaseProcessor;
import rubt.annotations.Annotations.Con;
import rubt.annotations.Annotations.Sendable;

import javax.annotation.processing.*;
import javax.lang.model.element.*;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.*;

@AutoService(Processor.class)
@SupportedAnnotationTypes("rubt.annotations.Annotations.Sendable")
public class SendableProc extends BaseProcessor {

    @Override
    public void process(RoundEnvironment env) throws Exception {
        var root = TypeSpec.classBuilder("Send").addModifiers(Modifier.PUBLIC);

        for (var packet : env.getElementsAnnotatedWith(Sendable.class)) {
            for (var field : packet.getEnclosedElements()) {

                // search for constructors with at least one argument
                if (field instanceof ExecutableElement exe && exe.getKind() == ElementKind.CONSTRUCTOR && !exe.getParameters().isEmpty()) {
                    var method = build(packet.asType().toString(), packet.getAnnotation(Sendable.class), exe);
                    root.addMethod(method);
                }
            }
        }

        // add some extra methods
        /*
         * public static void chatMessage(Player author, String message) {
         * new ChatMessage(author.name + "[coral]:[white] " + message).sendTCP();
         * }
         * public static void player() {
         * player.sendTCP(clientCon);
         * }
         */

        // sort methods
        root.methodSpecs.sort((m1, m2) -> m1.toString().compareTo(m2.toString()));

        write("rubt.net", root);
    }

    public MethodSpec build(String className, Sendable annotation, ExecutableElement constructor) {
        className = className.substring(className.lastIndexOf(".") + 1);

        var method = MethodSpec.methodBuilder(methodName(className)).addModifiers(Modifier.PUBLIC, Modifier.STATIC);
        var params = Seq.with(constructor.getParameters());

        if (annotation.connection() == Con.custom) // add an argument for connection
            method.addParameter(Connection.class, Con.custom.code);

        method.addParameters(params.map(ParameterSpec::get)); // copy arguments from the packet's constructor

        method.addStatement("new $T($L).send$L($L)",
                ClassName.get("rubt.net.Packets", className),                 // packet name
                params.toString(", ", arg -> arg.getSimpleName().toString()), // arguments
                annotation.reliable() ? "TCP" : "UDP",                        // protocol
                annotation.connection().code);                                // connection

        return method.build();
    }

    public String methodName(String className) {
        className = className.replaceFirst("Update", "").replaceFirst("Create", "");

        // change first char to lowercase letter
        return Character.toLowerCase(className.charAt(0)) + className.substring(1);
    }
}
