package it.s8.java_questions;

import tools.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public record IntrusionEvent(
    @JacksonXmlProperty(isAttribute = true, localName = "timestamp") long timestamp,
    String description) {}
