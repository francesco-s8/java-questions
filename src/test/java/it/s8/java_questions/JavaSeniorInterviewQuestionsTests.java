package it.s8.java_questions;

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
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@Slf4j
class JavaSeniorInterviewQuestionsTests {

  private static Stream<Arguments> stringsSmart() {
    return Stream.of(
        Arguments.of("hello", false), Arguments.of("Mom", true), Arguments.of("abc", false));
  }

  private List<User> stubUsersList() {
    var userOne = new User("John", "123");
    var userTwo = new User("Jane", "456");
    return List.of(userOne, userTwo);
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
    map.forEach((k, v) -> log.info("key-value is {}-{}", k, v));
    assertThat(map).hasSize(5).containsEntry(null, "null_").containsEntry("c", null);
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
    list.forEach(
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

  @ParameterizedTest(name = " Input:{index} => string={0}, isPalindrome={1}")
  @MethodSource("stringsSmart")
  void palindromeTest_smart(String first, boolean isPalindrome) {

    var sw = new StopWatch();
    sw.start();
    var result = isPalindromeSmart(first);
    sw.stop();
    log.info(
        "Time taken to check palindrome (smart method) in nanos : {}", sw.getDuration().toNanos());
    assertThat(result).isEqualTo(isPalindrome);

    assertThatThrownBy(() -> isPalindromeSmart(null))
        .isExactlyInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> isPalindromeSmart(""))
        .isExactlyInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> isPalindromeSmart("     "))
        .isExactlyInstanceOf(IllegalArgumentException.class);
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

  @Test
  @DisplayName("Test using filter stream method")
  void filterTest() {

    var input = stubUsersList();
    var actual = input.stream().filter(user -> "John".equals(user.name())).toList();
    assertThat(actual).hasSize(1).contains(stubUsersList().getFirst());
  }

  @Test
  @DisplayName("Simple test using map from Stream API")
  void streamMapTest() {
    var input = stubUsersList();

    var names = input.stream().map(User::name).toList();
    assertThat(names).isNotEmpty().hasSize(2);
  }

  Map<String, Long> countByOccurrence(final List<String> input) {

    return input.stream()
        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
  }

  @Test
  @DisplayName("Reinventing the wheel, defining a custom is Palindrome method")
  void customIsPalindrome() {

    // We should used parametrized test and dedicated unit tests for exceptions
    var input = "mom";
    assertThat(customIsPalindrome(input)).isTrue();
    var secondInput = "Mom";
    assertThat(customIsPalindrome(secondInput)).isTrue();
    var thirdInput = "John";
    assertThat(customIsPalindrome(thirdInput)).isFalse();
    var fourthInput = "   ";
    assertThatThrownBy(() -> customIsPalindrome(fourthInput))
        .isExactlyInstanceOf(IllegalArgumentException.class);
    var fifthInput = "";
    assertThatThrownBy(() -> customIsPalindrome(fifthInput))
        .isExactlyInstanceOf(IllegalArgumentException.class);

    assertThatThrownBy(() -> customIsPalindrome(null))
        .isExactlyInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("isPalindromeWithStopWatch with StopWatch")
  void isPalindromeWithStopWatch() {
    var input = "Tattarrattat";
    var watch = StopWatch.createStarted();
    var firstResult = customIsPalindrome(input);
    watch.stop();
    log.info(
        "Input {},Time taken to check palindrome (custom method) in nanos : {}",
        input,
        watch.getDuration().toNanos());

    assertThat(firstResult).isTrue();
    watch.reset();
    watch.start();
    isPalindromeSmart(input);
    watch.stop();
    log.info(
        "Input {},Time taken to check palindrome (smart method) in nanos : {}",
        input,
        watch.getDuration().toNanos());
    watch.reset();
    watch.start();
    var secondResult = isPalindromeSingleString(input);
    watch.stop();
    log.info(
        "Time taken to check palindrome (non custom method) in nanos : {}",
        watch.getDuration().toNanos());
    assertThat(secondResult).isTrue();
  }

  boolean customIsPalindrome(String input) {
    Objects.requireNonNull(input);
    var charArray = input.toLowerCase().toCharArray();
    if (input.isBlank()) {
      throw new IllegalArgumentException("String contains only whitespaces or is empty");
    }
    for (int i = 0; i < charArray.length; i++) {
      for (int j = charArray.length - 1; j > 0; j--) {
        if (charArray[i++] != charArray[j]) {
          return false;
        }
      }
    }
    return true;
  }

  boolean isPalindromeFromTwoStrings(String first, String second) {

    if (StringUtils.isBlank(first) || StringUtils.isBlank(second)) {
      throw new RuntimeException("One of two inputs is null or empty");
    }
    StringUtils.reverse(first);
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

  boolean isPalindromeSmart(String word) {

    if (word == null || word.isBlank()) {
      throw new IllegalArgumentException("Input is null or empty");
    }
    var wordLowerCase = word.toLowerCase();

    int start = 0;
    int end = word.length() - 1;
    while (start < end) {
      if (wordLowerCase.charAt(start) != wordLowerCase.charAt(end)) {
        return false;
      }
      start++;
      end--;
    }
    return true;
  }
}
