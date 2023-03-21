package com.example.search.entity.strategy;

import com.example.search.utils.StringUtils;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

public class PhysicalNamingStrategy implements org.hibernate.boot.model.naming.PhysicalNamingStrategy {
    @Override
    public Identifier toPhysicalCatalogName(Identifier logicalName, JdbcEnvironment jdbcEnvironment) {
        return convert(logicalName);
    }

    @Override
    public Identifier toPhysicalSchemaName(Identifier logicalName, JdbcEnvironment jdbcEnvironment) {
        return convert(logicalName);
    }

    @Override
    public Identifier toPhysicalTableName(Identifier logicalName, JdbcEnvironment jdbcEnvironment) {
        return convert(logicalName);
    }

    @Override
    public Identifier toPhysicalSequenceName(Identifier logicalName, JdbcEnvironment jdbcEnvironment) {
        return convert(logicalName);
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier logicalName, JdbcEnvironment jdbcEnvironment) {
        return convert(logicalName);
    }

    private Identifier convert(Identifier identifier) {
        if (identifier == null || StringUtils.isBlank(identifier.getText())) {
            return identifier;
        }
        String regex = "([a-z])([A-Z])";
        String replacement = "$1_$2";
        String newName = identifier.getText().replaceAll(regex, replacement).toLowerCase();
        return Identifier.toIdentifier(newName);
    }

}
