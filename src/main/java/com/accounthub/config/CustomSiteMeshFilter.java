package com.accounthub.config;

import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;

public class CustomSiteMeshFilter extends ConfigurableSiteMeshFilter {

    @Override
    protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {
        builder.setDecoratorPrefix("/WEB-INF/views/decorators/")
                .addDecoratorPath("/*", "web.jsp")
                .addDecoratorPath("/admin/*", "admin.jsp")
                .addDecoratorPath("/admin/**", "admin.jsp")
                .addDecoratorPath("/dashboard", "admin.jsp")
                .addDecoratorPath("/profile", "admin.jsp")
                .addExcludedPath("/login*")
                .addExcludedPath("/login/*")
                .addExcludedPath("/register*")
                .addExcludedPath("/register/*")
                .addExcludedPath("/alogin*")
                .addExcludedPath("/alogin/*")
                .addExcludedPath("/api/*")
                .addExcludedPath("/api/**")
                .addExcludedPath("/static/*")
                .addExcludedPath("/static/**")
                .addExcludedPath("/css/*")
                .addExcludedPath("/js/*")
                .addExcludedPath("/images/*")
                .addExcludedPath("/error*")
                .addExcludedPath("/h2-console/*");
    }
}
