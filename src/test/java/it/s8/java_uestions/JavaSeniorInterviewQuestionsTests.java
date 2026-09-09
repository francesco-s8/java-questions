package it.s8.java_uestions;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@Slf4j
class JavaSeniorInterviewQuestionsTests {

  private static Stream<Object> strings() {
    return Stream.of(Arguments.of("hello", "olleh", true), Arguments.of("Hello", "olleh", true));
  }

  @Test
  @DisplayName("This the Test of everything")
  void ultimateTest() {
    // Woah i am using Java record that's crazy!!OMG!!
    var user = new User("John", "Doe");
    assertThat(user).isNotNull();
  }

  @Test
  void testDatePlusFiveMonth() {

    LocalDate date = LocalDate.of(2021, 7, 15);
    var datePlusFiveMonth = date.plusMonths(5);
    assertThat(datePlusFiveMonth).isNotNull().isEqualTo(LocalDate.of(2021, 12, 15));
  }

  @Test
  void testOptionalWithStream() {

    Optional.of(List.of("1")).stream()
        .flatMap(List::stream)
        .findFirst()
        .ifPresent(el -> log.info("first element is {}", el));
  }

  @Test
  @DisplayName("The lazy test,maybe i should write my own method...")
  void testExtractSubstringBetweenSlashes() {

    var input = "/hello/world/";
    var actual = StringUtils.substringBetween(input, "/", "/");
    assertThat(actual).isEqualTo("hello");
  }

  @Test
  void workingWithHashMap() {

    Map<String, String> map = new HashMap<>();
    map.put("a", "a");
    map.put("b", "b");
    map.put(null, "null_");
    map.put("c", null);
    map.put("d", null);
    log.info("map size is {}", map.size());
    map.forEach((k, v) -> log.info("key-value is {}-{}", k, v));
  }

  @Test
  void unique() {

    var strings = List.of("a", "b", "c", "d", "c", "d");
    var filtered = strings.stream().distinct().toList();
    assertThat(filtered).hasSize(4);
  }

  @Test
  void continueLoop() {

    var list = Stream.of("1", "abc", "2");
    list.forEachOrdered(
        el -> {
          try {
            log.info("Number converted is {}", Integer.valueOf(el));
          } catch (NumberFormatException e) {
            log.error("Exception occurred ", e);
          }
        });
  }

  @Test
  void wordsCountingInAnArray() {

    var input = List.of("a", "b", "c", "a", "b");
    var result = countByOccurrence(input);
    log.info("res is {}", result);
    assertThat(result).hasSize(3).contains(entry("a", 2L));
    List<String> emptyInput = List.of();
    var actual = countByOccurrence(emptyInput);
    log.info("Empty counting is {}", actual);
    assertThat(actual).isEmpty();
  }

  @Test
  void stringDifferentObjectInstances() {
    // How String pool works...
    var first = new String("hello");
    var second = new String("hello");
    assertThat(first).isNotSameAs(second);
  }

  @Test
  @DisplayName("Just a random test using the brand new Java API")
  void monthsBetween() {
    var start = LocalDate.of(2016, 5, 1);
    var end = LocalDate.of(2019, 12, 1);
    var monthsBetween = Period.between(start, end).toTotalMonths();
    log.info("months between {} and {} is {}", start, end, monthsBetween);
    assertThat(monthsBetween).isGreaterThan(1L);
  }

  private Map<String, Long> countByOccurrence(final List<String> input) {

    return input.stream()
        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
  }

  @ParameterizedTest(name = " Input:{index} => firstString={0}, secondString={1}, isPalindrome={2}")
  @MethodSource("strings")
  void palindromeTest_official(String first, String second, boolean isPalindrome) {

    assertThat(isPalindromeFromTwoStrings(first, second)).isEqualTo(isPalindrome);
  }

  @Test
  void palindromeTest_theSecond() {
    var a = StringUtils.EMPTY;
    var b = "ciao";
    assertThatThrownBy(() -> isPalindromeFromTwoStrings(a, b))
        .isExactlyInstanceOf(RuntimeException.class);
  }

  @Test
  void palindromeTestSingleString() {
    var input = "Mom";
    assertThat(isPalindromeSingleString(input)).isTrue();
  }

  @Test
  void palindromeTestEmptyAndNullString() {
    var input = "";
    assertThatThrownBy(() -> isPalindromeSingleString(input))
        .isExactlyInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> isPalindromeSingleString(null))
        .isExactlyInstanceOf(IllegalArgumentException.class);
  }

  boolean isPalindromeFromTwoStrings(String first, String second) {

    if (StringUtils.isBlank(first) || StringUtils.isBlank(second)) {
      throw new RuntimeException("One of two inputs is null or empty");
    }
    if (first.length() != second.length()) {
      return false;
    }
    return first.toLowerCase().contentEquals(new StringBuilder(second.toLowerCase()).reverse());
  }

  boolean isPalindromeSingleString(String toCheck) {
    if (toCheck == null || toCheck.isBlank()) {
      throw new IllegalArgumentException("Input is null or empty");
    }
    return toCheck.toLowerCase().contentEquals(new StringBuilder(toCheck.toLowerCase()).reverse());
  }
}
