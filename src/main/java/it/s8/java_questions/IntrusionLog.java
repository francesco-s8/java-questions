package it.s8.java_questions;

import java.util.List;
import tools.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import tools.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public record IntrusionLog(
    @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "event")
        List<IntrusionEvent> events) {}
