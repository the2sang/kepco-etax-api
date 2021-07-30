package com.kepco.etax.openapi;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.kepco.etax.openapi");

        noClasses()
            .that()
            .resideInAnyPackage("com.kepco.etax.openapi.service..")
            .or()
            .resideInAnyPackage("com.kepco.etax.openapi.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..com.kepco.etax.openapi.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
