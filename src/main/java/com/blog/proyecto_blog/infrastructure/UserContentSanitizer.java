package com.blog.proyecto_blog.infrastructure;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.springframework.stereotype.Component;

@Component
public class UserContentSanitizer {

    private static final PolicyFactory NO_HTML_POLICY =
            new HtmlPolicyBuilder().toFactory();

    public String sanitizerMarkdown(String content) {
        return NO_HTML_POLICY.sanitize(content);
    }

    public String sanitizerPlainText(String content) {
        return NO_HTML_POLICY.sanitize(content);
    }
}
