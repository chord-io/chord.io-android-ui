package io.chord.generators;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import io.swagger.codegen.v3.CodegenOperation;
import io.swagger.codegen.v3.generators.java.JavaClientCodegen;

public class JavaClientGenerator extends JavaClientCodegen
{
    private static final Logger LOGGER = Logger.getLogger(
            KotlinClientGenerator.class.getName()
    );

    @Override
    public Map<String, Object> postProcessOperations(Map<String, Object> objs) {
        super.postProcessOperations(objs);
        Map<String, Object> operations = (Map<String, Object>) objs.get("operations");
        List<CodegenOperation> ops = (List<CodegenOperation>) operations.get("operation");
        for (CodegenOperation operation : ops) {
            if (operation.returnType.equals("Void")) {
                operation.returnType = null;
            }
            else if(operation.returnType.contains("List"))
            {
                operation.returnType = operation.returnType.replace("&lt;", "<");
                operation.returnType = operation.returnType.replace("&gt;", ">");
            }
        }
        return objs;
    }
}
