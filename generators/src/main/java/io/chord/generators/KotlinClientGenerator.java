package io.chord.generators;


import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import io.swagger.codegen.v3.CodegenModel;
import io.swagger.codegen.v3.generators.kotlin.KotlinClientCodegen;
import io.swagger.v3.oas.models.media.Schema;

public class KotlinClientGenerator extends KotlinClientCodegen {
    private static final Logger LOGGER = Logger.getLogger(
        KotlinClientGenerator.class.getName()
    );

    public KotlinClientGenerator() {
        super();
    }

    @Override
    public String escapeReservedWord(String name) {
        if(name.equals("Object"))
        {
            return name;
        }

        return super.escapeReservedWord(name);
    }

    @Override
    public CodegenModel fromModel(String name, Schema schema, Map<String, Schema> allDefinitions) {
        CodegenModel model = super.fromModel(name, schema, allDefinitions);

        if(name.equals("Object"))
        {
            model.classname = "Object";
        }

        model.allVars
            .forEach(x -> {
                if(x.datatype.contains("object"))
                {
                    x.datatype = x.datatype.replace(
                        "object",
                        "Object"
                    );
                    x.datatype = x.datatype.replace(
                            "`",
                            ""
                    );
                }

                if(x.datatype.contains("LocalDateTime"))
                {
                    x.datatype = x.datatype.replace(
                            "LocalDateTime",
                            "ZonedDateTime"
                    );
                }

                if(x.datatype.contains("Array"))
                {
                    x.datatype = x.datatype.replace(
                            "Array",
                            "List"
                    );
                }

                if(x.datatype.contains("kotlin."))
                {
                    x.datatype = x.datatype.replace(
                            "kotlin.",
                            ""
                    );
                }

                if(x.datatype.contains("collections."))
                {
                    x.datatype = x.datatype.replace(
                            "collections.",
                            ""
                    );
                }
            });

        if(!model.getIsEnum() && model.parentSchema != null)
        {
            model.requiredVars = model.requiredVars
                    .stream()
                    .distinct()
                    .filter(model.vars::contains)
                    .collect(Collectors.toList());

            model.optionalVars = model.optionalVars
                    .stream()
                    .distinct()
                    .filter(model.vars::contains)
                    .collect(Collectors.toList());

            Schema parent = allDefinitions.get(model.parentSchema);
            parent.setName(model.parentSchema);
            CodegenModel parentModel = super.fromModel(
                    parent.getName(),
                    parent,
                    allDefinitions
            );

            model.parentVars = parentModel.allVars;
        }

        model.allVars
            .forEach(x -> {
                if(!x.required)
                {
                    x.datatype += "?";
                    x.defaultValue = "null";
                }
                else
                {
                    x.datatype = x.datatype.replace("?", "");
                    x.defaultValue = "";
                }
            });

        return model;
    }

    @Override
    protected void addImport(CodegenModel m, String type) {
        if(type != null && this.needToImport(type) && type.contains("object"))
        {
            m.imports.add("Object");
            return;
        }

        super.addImport(m, type);
    }
}
