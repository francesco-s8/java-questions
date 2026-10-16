package it.s8.java_questions;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

  private static final Pattern timestampPattern = Pattern.compile("timestamp=\"(\\d+)\"");

  private static Stream<Arguments> stringsSmart() {
    return Stream.of(
        Arguments.of("hello", false), Arguments.of("Mom", true), Arguments.of("abc", false));
  }

  private List<User> stubUsersList() {
    var userOne = new User("John", "123");
    var userTwo = new User("Jane", "456");
    return List.of(userOne, userTwo);
  }

  private Collection<Long> getTimestampsByDescription(String xml, String description) {

    ArrayList<Long> times = new ArrayList<>();
    var split = Arrays.stream(xml.split("\n")).toList();

    for (int i = 0; i < split.size(); i++) {
      var line = split.get(i);
      if (line.contains(description)) {
        var l = split.get(i - 1);
        Matcher matcher = timestampPattern.matcher(l);
        if (matcher.find()) {
          long timestamp = Long.parseLong(matcher.group(1));
          times.add(timestamp);
        }
      }
    }
    return times;
  }

  @Test
  @DisplayName("This the Test of everything")
  void ultimateTest() {
    // Woah I am using Java record that's crazy!!OMG!!
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
        .ifPresent(el -> JavaSeniorInterviewQuestionsTests.log.info("first element is {}", el));
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
    map.forEach((k, v) -> JavaSeniorInterviewQuestionsTests.log.info("key-value is {}-{}", k, v));
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
            JavaSeniorInterviewQuestionsTests.log.info(
                "Number converted is {}", Integer.valueOf(el));
          } catch (NumberFormatException e) {
            JavaSeniorInterviewQuestionsTests.log.error("Exception occurred ", e);
          }
        });
  }

  @Test
  void wordsCountingInAnArray() {

    var input = List.of("a", "b", "c", "a", "b");
    var result = countByOccurrence(input);
    JavaSeniorInterviewQuestionsTests.log.info("res is {}", result);
    assertThat(result).hasSize(3).contains(entry("a", 2L));
    List<String> emptyInput = List.of();
    var actual = countByOccurrence(emptyInput);
    JavaSeniorInterviewQuestionsTests.log.info("Empty counting is {}", actual);
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
    JavaSeniorInterviewQuestionsTests.log.info(
        "months between {} and {} is {}", start, end, monthsBetween);
    assertThat(monthsBetween).isGreaterThan(1L);
  }

  @ParameterizedTest(name = " Input:{index} => string={0}, isPalindrome={1}")
  @MethodSource("stringsSmart")
  void palindromeTest_smart(String first, boolean isPalindrome) {

    var sw = new StopWatch();
    sw.start();
    var result = isPalindromeSmart(first);
    sw.stop();
    JavaSeniorInterviewQuestionsTests.log.info(
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
    JavaSeniorInterviewQuestionsTests.log.info(
        "Input {},Time taken to check palindrome (custom method) in nanos : {}",
        input,
        watch.getDuration().toNanos());

    assertThat(firstResult).isTrue();
    watch.reset();
    watch.start();
    isPalindromeSmart(input);
    watch.stop();
    JavaSeniorInterviewQuestionsTests.log.info(
        "Input {},Time taken to check palindrome (smart method) in nanos : {}",
        input,
        watch.getDuration().toNanos());
    watch.reset();
    watch.start();
    var secondResult = isPalindromeSingleString(input);
    watch.stop();
    JavaSeniorInterviewQuestionsTests.log.info(
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

  String numbersToLetters(String s) {
    if (s == null || s.isBlank()) {
      throw new IllegalArgumentException("Input is null or empty");
    }

    var result = new StringBuilder();
    var list = new ArrayList<>(Arrays.asList(s.split("[\\s+]+")));

    list.forEach(
        el -> {
          var number = Integer.parseInt(el);
          if (number < 1 || number > 26) {
            throw new IllegalArgumentException("Number out of range: " + number);
          }
          result.append((char) ('A' + number - 1));
        });

    return result.toString();
  }

  @Test
  void fromStringOfNumbersToLetters() {
    var in = "20 5 19 20+4 15 13 5";
    var actual = numbersToLetters(in);
    assertThat(actual).isEqualTo("TESTDOME");
  }

  @Test
  void intrusions() {
    String xml =
        """
            <?xml version="1.0" encoding="UTF-8"?>
            + "<log>
            + "<event timestamp="1614285589">
            + "<description>Intrusion detected</description>
            + "</event>
            + "<event timestamp="1614286432">
            + "<description>Intrusion ended</description>
            + "</event>
            + "</log>
        """;

    Collection<Long> timestamps = getTimestampsByDescription(xml, "Intrusion ended");
    assertThat(timestamps).containsExactly(1614286432L);
    assertThat(getTimestampsByDescription(xml, "Intrusion detected")).containsExactly(1614285589L);
  }

  @Test
  void checkWordOccurrence() {

    var sp = StopWatch.createStarted();
    var text =
"""
Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce sodales vestibulum nibh, non sollicitudin massa sodales a. Proin vestibulum ut lacus a volutpat.
Ut porta ut metus sed euismod. Duis eget libero odio. In ornare cursus sem non dignissim. Proin interdum ipsum et odio mollis, ut facilisis lacus placerat. Ut luctus risus at nunc convallis rutrum.
Nunc tellus quam, pellentesque ut purus eu, pulvinar mollis nisi. Curabitur eu metus lorem.
Nullam efficitur vulputate sapien. Nunc quis bibendum eros. Maecenas viverra tincidunt urna, quis efficitur tellus suscipit nec. In sit amet feugiat neque.
In eget dui maximus, consequat orci sit amet, scelerisque arcu. Duis massa ex, imperdiet eu velit eu, ultrices scelerisque urna.
Lorem.
""";

    var text2 = "Lorem. Lorem";

    var text3 =
"""
Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec iaculis dapibus metus at molestie. Phasellus laoreet vestibulum velit a posuere. Nullam vehicula arcu a augue iaculis ullamcorper. Aliquam ac sapien lobortis, ultricies tellus quis, mollis nulla. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Duis imperdiet lacinia ullamcorper. Quisque tellus erat, gravida quis pellentesque ut, cursus eu dui. Pellentesque a tellus in quam scelerisque finibus sit amet a ante. Aenean interdum molestie ex sed gravida. Nulla vestibulum turpis id hendrerit condimentum. Etiam sagittis lorem eros, sed elementum magna mollis sit amet.

Cras convallis feugiat ligula quis semper. Morbi ligula tortor, faucibus vitae ultrices at, commodo et velit. Sed efficitur tristique libero lacinia efficitur. Ut eget hendrerit ipsum. Duis nulla justo, tempus vel velit at, volutpat convallis arcu. Nullam hendrerit nunc commodo augue imperdiet, eu placerat erat ultricies. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Ut ultricies eros ac velit mattis, vel aliquam lacus varius. Aenean eu nisl convallis, congue justo non, condimentum dui. Cras neque elit, fringilla a posuere quis, tincidunt eget sem. Donec eleifend velit nisl, eu venenatis sapien tempus quis.

Curabitur at pulvinar ante. Sed nec lacus nec tellus imperdiet finibus at a arcu. Etiam sit amet finibus lorem, quis pretium tellus. Donec et tristique tellus. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Proin imperdiet tincidunt sem, sit amet lobortis velit interdum vel. Cras faucibus posuere nisl, vitae hendrerit tortor. Nullam mauris justo, cursus eu magna a, elementum commodo ex. Phasellus massa turpis, mattis et nunc quis, pulvinar ultricies ante. Quisque nisl enim, placerat ut leo ut, consectetur tempor ipsum. Nunc pharetra cursus tortor at commodo.

Aenean in eros laoreet, volutpat lorem in, elementum dui. Proin purus neque, congue quis fringilla ac, elementum vitae nulla. Etiam scelerisque et nisi eu mollis. Sed urna ipsum, rutrum a justo in, scelerisque laoreet eros. Nulla maximus augue quis porta vulputate. Donec fermentum rutrum nisi, nec hendrerit nulla pulvinar sit amet. Ut nec ornare neque. Proin consectetur, mi sed dapibus venenatis, tellus ex iaculis odio, et condimentum arcu ante et odio. Morbi eget nunc hendrerit, feugiat risus rhoncus, tincidunt metus. Etiam ut tincidunt massa. Nam vel felis dignissim, faucibus leo et, pharetra augue. Aenean aliquam ornare ligula, ut iaculis diam consectetur id. Aliquam vel laoreet eros, cursus rhoncus lacus.

Suspendisse vel eros dapibus, consectetur ipsum et, placerat neque. Donec ac leo libero. Quisque auctor tempus velit, nec condimentum lacus faucibus sit amet. Maecenas tempor sollicitudin ante, vel ornare mi fringilla quis. Sed non aliquet nunc, pharetra iaculis nibh. Nunc pretium quam id nulla porta, vitae pellentesque nisl tempor. Donec fringilla finibus ultricies. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Pellentesque eget dapibus sem. Maecenas pulvinar, nisl vitae sodales pellentesque, arcu ante pellentesque purus, a rhoncus dui ex vitae nisi. Aliquam suscipit ut felis fringilla sodales. Curabitur facilisis aliquam leo, eget cursus massa finibus ac. Fusce auctor urna purus, eu rhoncus nulla rutrum varius. Pellentesque quis velit dolor.

Sed rutrum arcu porttitor, consectetur tellus sit amet, vehicula urna. Suspendisse egestas scelerisque ligula at aliquam. Praesent tortor metus, efficitur semper ligula ut, eleifend mattis magna. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Integer eget diam pretium, pharetra massa quis, commodo sem. Cras nec rutrum diam, sed aliquet eros. Sed et urna sed tortor aliquet elementum. Donec efficitur ac odio id blandit. In in efficitur mi, id auctor lacus. Praesent ac auctor lectus. Curabitur eget cursus nulla, quis placerat massa.

Aenean faucibus eleifend dui. Maecenas turpis sapien, varius nec venenatis in, malesuada sed ex. Sed eleifend eros metus, ornare consectetur ipsum blandit ut. Donec fringilla nulla nec viverra venenatis. Curabitur accumsan dolor purus, quis ullamcorper urna viverra malesuada. Sed euismod malesuada dictum. In id dignissim nisi. Duis nec turpis porta, finibus lacus ut, ultricies mauris. Etiam urna diam, gravida at elit dignissim, sagittis venenatis ligula. Nam enim dui, sagittis at semper eu, elementum eget enim. Fusce aliquet odio vitae pellentesque lacinia. Vivamus in justo lectus.

Maecenas eu metus urna. Nam at ullamcorper metus. Ut auctor dolor ut odio vulputate facilisis. Quisque ut pellentesque velit, ac luctus nisi. Ut in lectus eget erat scelerisque facilisis nec ut dolor. Etiam placerat felis eu facilisis vestibulum. Praesent rutrum risus leo, vitae interdum tellus ullamcorper a. Fusce quis commodo tortor. Mauris facilisis suscipit aliquet. Lorem ipsum dolor sit amet, consectetur adipiscing elit.

Cras ac neque diam. Curabitur sit amet dapibus justo. Curabitur a ligula turpis. Sed eu porttitor turpis. Pellentesque quis lorem turpis. In lobortis luctus quam et consequat. Fusce lacinia erat nunc. Aenean eu mauris vel tortor tincidunt rutrum vitae ut est. Quisque fringilla lorem sit amet luctus malesuada. Quisque id vehicula felis. Maecenas vel mauris elit. Nam accumsan, erat et maximus sollicitudin, ipsum sem consectetur elit, eget rhoncus mauris nisl eget est.

Etiam molestie consequat congue. Vivamus cursus metus leo, eget semper urna accumsan id. Aenean in enim velit. Aliquam mollis ullamcorper eros et posuere. Proin nec pellentesque diam, sit amet mattis dolor. Donec id lacus sit amet diam imperdiet varius. Donec quis pharetra ante, porta rutrum mauris. Nunc ornare, nibh non vestibulum aliquet, nunc diam aliquam nibh, a dictum leo orci sed leo. Aenean luctus dui mi, non tincidunt eros lacinia et. Praesent elementum magna risus, eu lobortis ligula porta sed. Ut pharetra congue nulla, non lacinia lectus porttitor et.

Sed eu sem vel libero lobortis lobortis ut vitae libero. Proin nulla tellus, vulputate lobortis finibus a, consectetur sed risus. Nulla lacinia est et felis tincidunt dapibus. Praesent auctor lorem quis velit fermentum dictum. Quisque consectetur, libero vel placerat dignissim, nibh mi blandit augue, at efficitur enim leo ac lorem. Fusce ornare vestibulum magna, nec iaculis justo. Pellentesque nisi augue, vehicula ornare turpis eu, blandit molestie augue. Duis eu nunc eu nisi semper tempus ac eget velit. Phasellus volutpat, dolor in tincidunt dictum, nibh nisl pulvinar ante, ut tincidunt magna lorem id lacus. Sed posuere accumsan metus, imperdiet pellentesque nunc tempus at. Nulla ullamcorper felis eget fermentum imperdiet. In interdum tincidunt augue.

Vivamus eget vehicula enim. Duis iaculis ante quis erat pulvinar, vitae pretium dui mattis. Duis vulputate velit eu quam finibus, maximus feugiat metus tempus. Nullam blandit elit a tortor lobortis, nec luctus est vestibulum. Etiam rutrum semper purus, non vehicula erat pulvinar eget. Proin ligula neque, suscipit vitae pulvinar eget, condimentum sed tortor. Sed quis felis vitae diam vehicula dictum. Donec mi risus, efficitur nec semper id, interdum ut lectus. Vivamus ac molestie nulla. Suspendisse mollis mi eu efficitur congue. Maecenas vitae hendrerit diam, id gravida justo. Vivamus accumsan mollis nisi in tempor. Suspendisse id convallis nulla, ac auctor lectus. Vestibulum sollicitudin ligula ut lectus suscipit fermentum. Fusce ex arcu, ultricies ut metus id, feugiat vulputate tellus. Curabitur tempus tellus sed ligula dictum, ut dictum arcu tempor.

Donec vitae sagittis risus. Integer pellentesque porttitor maximus. Nunc in luctus magna, quis maximus nisl. Duis rutrum tincidunt sodales. Pellentesque maximus volutpat nisi a pretium. Curabitur lobortis justo eu tempus pretium. Nullam euismod nisl non augue rhoncus efficitur. Praesent venenatis accumsan metus, ac pretium neque imperdiet molestie.

Phasellus eleifend nec odio eget ultricies. Mauris in justo vitae leo mattis feugiat. Sed in lectus semper, lacinia lorem non, suscipit lorem. Sed tincidunt, dui in condimentum convallis, orci sapien lobortis elit, id tincidunt neque lorem sit amet ipsum. Vivamus neque risus, facilisis nec laoreet eu, cursus eu metus. Quisque dui odio, porttitor in augue sed, rutrum ornare leo. Praesent vel tellus malesuada sapien commodo gravida. Integer molestie orci sed nisl tristique dapibus sit amet et nunc. Vestibulum accumsan turpis id nisl tristique dignissim. Phasellus efficitur orci commodo nibh varius, ut molestie diam fringilla.

Praesent quis aliquam tortor, bibendum eleifend tortor. Phasellus venenatis nec nisi sit amet sollicitudin. Nullam hendrerit leo quis nisi vulputate, nec congue metus faucibus. Sed ut massa pharetra, ultrices nunc eget, vehicula ipsum. Vivamus commodo pharetra turpis non interdum. Curabitur nisi ipsum, tempus et erat nec, tempor semper felis. Etiam elementum mauris erat, id placerat ligula imperdiet ac. Vestibulum pulvinar, eros eu ornare semper, ante odio efficitur felis, hendrerit vehicula purus lacus eu sapien. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent malesuada, ante in mollis pulvinar, diam ex pharetra erat, at volutpat nisl sapien vitae diam. Pellentesque lectus lacus, eleifend vel venenatis nec, laoreet ut felis. Curabitur at metus aliquet, finibus sapien a, accumsan lectus. Suspendisse lacinia dignissim lectus, in bibendum sapien laoreet ut. Donec facilisis odio neque, eget blandit magna luctus laoreet.

Suspendisse potenti. Nulla pulvinar, felis ut vulputate dapibus, sem orci ultrices leo, vel efficitur nulla eros nec urna. Mauris fringilla sem quam, id semper dui pretium ac. Aliquam aliquam felis nulla, at convallis mi ullamcorper vel. Aliquam nec erat pulvinar, porta quam eget, convallis nulla. Proin suscipit neque quis quam condimentum tempus. Suspendisse ac dolor gravida, suscipit arcu vel, ultricies est. Praesent convallis varius eros eu sollicitudin. Nulla odio ipsum, elementum lacinia nisi vitae, dictum luctus massa. Nunc sagittis elit at elit porttitor, sed pellentesque nulla tincidunt. Integer dui magna, lobortis quis facilisis eu, euismod vitae neque. Cras posuere nunc metus, sit amet mattis ligula convallis a. Praesent id diam metus.

Aliquam erat volutpat. Maecenas congue sollicitudin accumsan. Donec efficitur gravida rhoncus. Donec sollicitudin condimentum tellus at eleifend. Nunc vel arcu tempus, rutrum nulla a, tincidunt turpis. Proin blandit fermentum elit, at sagittis sem tempor sit amet. Mauris vitae ligula quis lorem venenatis iaculis pharetra vitae nisl.

Donec nulla dui, faucibus ac malesuada quis, consectetur ac neque. Praesent consequat lacinia blandit. Etiam congue urna velit, ac scelerisque nulla vehicula dictum. Nunc non felis blandit urna dapibus placerat. Vivamus mi neque, vehicula in pharetra sit amet, porta sit amet magna. In nec ante non neque eleifend aliquet. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Nunc eget ligula at turpis efficitur iaculis ut a tortor. Ut id dui commodo, pellentesque magna et, ultrices neque. Praesent accumsan, sapien blandit tempus efficitur, felis nibh accumsan sem, eu luctus diam sem id diam. In et lectus eget quam pulvinar iaculis non id massa. Etiam posuere justo tincidunt augue tristique, sed convallis velit viverra. In sed lacus nec lectus fringilla luctus. Pellentesque nec tellus sit amet sem faucibus dignissim eget in nibh.

Sed vitae dapibus nunc. Donec vel enim diam. In hendrerit leo quis tortor fermentum vulputate. In consectetur nisl arcu, eu mollis est suscipit ac. Nunc maximus neque ut est tempor cursus. Suspendisse vel tristique arcu, eget semper nibh. Nam sed arcu eu dui volutpat condimentum. Quisque feugiat venenatis magna, vulputate facilisis lectus pellentesque ac. Sed vel purus ut sem consequat posuere. Ut scelerisque, nunc et ultricies viverra, orci ex vulputate mauris, vitae placerat magna metus quis lacus. Cras dictum, leo ut luctus lobortis, arcu metus lacinia metus, eu elementum ipsum massa ut magna. Maecenas ante elit, euismod vel egestas eu, rhoncus ut ex. In dapibus quam feugiat lectus commodo, sed venenatis nisi mollis. Cras convallis pretium elementum. Pellentesque ullamcorper ut magna eget sodales. Praesent lacinia arcu eu urna interdum imperdiet.

Aliquam posuere non risus sit amet molestie. Curabitur sed semper enim. Praesent odio odio, fringilla vel diam quis, vehicula facilisis justo. Vivamus velit lacus, tristique ac dignissim a, viverra non ipsum. Vivamus arcu nisi, laoreet vitae dapibus at, interdum quis ligula. Donec id est at metus pharetra egestas id non lorem. Duis malesuada viverra iaculis. Aliquam mattis, elit non suscipit aliquam, arcu dolor bibendum libero, nec vehicula dolor tellus eget mi. Ut non pulvinar turpis. Quisque vehicula dui et lorem pharetra, at imperdiet nunc porttitor. Proin tincidunt pharetra dolor eu pretium. Quisque id ante maximus, dictum diam vel, elementum eros. Maecenas eget libero tortor.

Vivamus quam ex, vestibulum consectetur justo a, ullamcorper ullamcorper risus. Donec volutpat ex nec venenatis placerat. Praesent ex mauris, consectetur at vehicula et, placerat vestibulum elit. Sed dapibus ligula et sapien ornare venenatis et nec sem. Sed scelerisque tempus eros. Pellentesque volutpat, justo vel tincidunt euismod, metus mauris tempor lacus, id auctor leo neque id nisi. Nullam eget odio tincidunt, tempor neque sit amet, rhoncus tellus. Donec velit tellus, dignissim vel ipsum sit amet, porttitor placerat est. Nulla facilisi. Integer non justo et felis rutrum feugiat. Vestibulum sagittis varius purus id sollicitudin. Quisque fringilla sapien eu magna dignissim euismod. Proin tincidunt, metus porttitor convallis porta, diam quam posuere magna, at vestibulum urna sem vel ligula. Curabitur ipsum ante, consectetur ac euismod non, interdum vitae erat. Integer auctor eu velit et tincidunt.

Etiam volutpat, nisl at tincidunt bibendum, neque nisi tincidunt magna, vitae maximus nunc odio finibus lacus. Phasellus vel nisi massa. Donec interdum, est ut rutrum congue, ante quam vestibulum purus, in tempus libero sem in nisl. Curabitur consequat varius nisl euismod semper. Ut nec mauris ut diam hendrerit sollicitudin non a diam. Nulla elementum vestibulum velit, et lobortis ante fermentum ut. Nunc arcu risus, vehicula vel purus id, dictum molestie lectus. Curabitur nec lectus leo. Etiam condimentum viverra ornare. Nulla feugiat ultrices elit sit amet sagittis. Maecenas sem mauris, ultrices ut mi et, tempus congue odio. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae;

In dignissim quam accumsan, interdum ante vel, euismod ipsum. Suspendisse commodo nisi a nulla dapibus, et maximus ex tincidunt. Pellentesque lobortis lacinia porta. Ut aliquam iaculis volutpat. Phasellus congue in arcu vitae mollis. Sed ullamcorper pharetra leo, at placerat metus. Quisque non ante sed velit luctus sodales. Donec quis sapien et erat gravida suscipit vitae ac velit. Nam feugiat lorem tellus, sed sodales dui finibus vitae. Curabitur pharetra id tellus in facilisis. Integer non urna orci. Nam laoreet porttitor tellus a viverra. Pellentesque eget tortor at ipsum blandit congue ac eu est. Aenean interdum risus eu nisl euismod finibus. Sed finibus placerat neque, sed consectetur eros. Proin et mauris sit amet magna commodo finibus.

Sed enim ipsum, efficitur a augue cursus, vestibulum mollis risus. Nulla a elit feugiat, scelerisque orci congue, gravida dui. Mauris erat quam, faucibus quis leo a, dignissim porta tortor. Sed porta, justo blandit lacinia dignissim, ipsum enim hendrerit quam, nec laoreet augue quam sed sem. Integer ut enim nulla. Fusce vitae fringilla nisi, non volutpat dui. Vivamus accumsan mi ac blandit vestibulum. Phasellus in egestas lectus. Maecenas porta tristique iaculis. Phasellus scelerisque orci sed tempus sollicitudin. Curabitur quis dignissim felis. Ut tincidunt quis ipsum ut fringilla. Sed facilisis nisl vitae dolor pellentesque viverra non nec sapien.

Integer vehicula eros sollicitudin felis dapibus molestie. Aenean sed velit ut justo accumsan laoreet id sollicitudin purus. Duis vitae purus sapien. Nunc vitae nisi ut mauris sagittis varius. Donec euismod cursus nulla dignissim auctor. Cras sed maximus turpis, vel suscipit nisi. Fusce ullamcorper bibendum rhoncus. Vivamus ornare odio leo, ut sagittis dui tempus ac. Pellentesque consequat aliquet facilisis. Praesent consectetur velit vitae quam egestas, eu vulputate est scelerisque. Vivamus facilisis odio a nisl maximus blandit. Ut dapibus at ipsum eleifend eleifend.

Donec blandit vitae justo eu elementum. Curabitur lobortis mauris nec rhoncus laoreet. Cras maximus sodales nulla, eget cursus orci ornare id. Integer et pharetra diam. Proin in fringilla augue, nec blandit ante. Nulla porta varius sem, a malesuada ligula varius non. Integer feugiat vulputate ultricies. Sed ante nulla, ultrices et rhoncus non, egestas sit amet ligula. In at massa augue. Sed libero lorem, posuere a condimentum id, porta a ipsum. Nam sed nisl quis lectus porta varius. Cras ut orci arcu. Phasellus sagittis mattis leo. Vestibulum erat odio, dictum vel semper quis, tincidunt a neque. Donec nec commodo risus, sit amet lobortis odio. Praesent vitae elit dolor.

Suspendisse interdum nulla tortor, at accumsan sem tristique sit amet. Etiam erat tellus, ullamcorper in arcu a, ultrices tincidunt quam. Cras nec hendrerit diam, a pellentesque diam. Morbi tincidunt est ut dui dignissim, vitae auctor arcu laoreet. Integer iaculis porta sem, non sollicitudin dui porta ut. Phasellus sodales metus ipsum, in accumsan nisi vehicula ut. Donec faucibus faucibus mi eget congue. Nunc tempor odio eget sapien efficitur, ut tincidunt nunc consequat. Pellentesque nec condimentum elit. Mauris pharetra vulputate nulla, at gravida magna tincidunt nec. Curabitur vehicula lacus eros, ac aliquam dui accumsan nec. Vestibulum porttitor consequat imperdiet. Vivamus et gravida turpis, non dapibus enim. Etiam fermentum quam rutrum venenatis molestie.

Aliquam sit amet porta neque, in suscipit ligula. Sed imperdiet, sapien et bibendum bibendum, sem libero auctor enim, ut eleifend quam dui nec magna. Proin sodales leo metus, id venenatis sem ornare non. Sed in dolor id justo facilisis mollis. Vivamus quis ex tempus, sollicitudin sem vel, elementum erat. Curabitur a accumsan justo. Nunc pellentesque varius tellus, id consectetur lectus sollicitudin aliquam. Ut ut nulla convallis, dictum turpis et, hendrerit est. Aliquam tellus quam, lobortis quis elit vitae, luctus venenatis enim. Donec fermentum augue vel metus imperdiet, a tempor ex sodales. Mauris at turpis leo. Ut iaculis, velit eget feugiat vehicula, metus sem interdum enim, eu sodales sem purus non libero. Donec interdum erat massa, vitae mollis ex pellentesque eu. Vestibulum vulputate nibh sed justo auctor, a pharetra elit consectetur. Aenean sed tortor turpis. Morbi mollis, sem sodales mattis condimentum, justo mi viverra lacus, ut elementum nulla nulla a sem.

Nullam diam magna, tincidunt sit amet justo eu, dictum accumsan nisl. Proin ex turpis, accumsan eu commodo et, maximus ac ex. Aliquam hendrerit lobortis nibh et venenatis. Fusce eleifend ex a felis molestie finibus. Morbi eget sem aliquam sapien iaculis porta. Integer tincidunt, lacus eu pulvinar elementum, orci neque tristique justo, a dapibus arcu lectus venenatis lacus. Nullam semper sit amet diam at commodo. Suspendisse id tristique arcu. Proin convallis, libero vel vestibulum condimentum, eros nibh malesuada sapien, eu placerat arcu metus eget leo. Fusce venenatis laoreet mi, sed feugiat sapien volutpat eget. Nullam sit amet arcu in ex dictum ullamcorper. Praesent massa dolor, semper euismod fringilla molestie, sodales quis quam. Phasellus elementum congue blandit.

Quisque pretium mauris ut orci iaculis, ut porta lacus ultricies. Nullam arcu dui, tincidunt vulputate bibendum eget, condimentum quis diam. Donec quis rutrum urna, eget tempor eros. Ut rhoncus velit quis erat dignissim, quis bibendum ipsum accumsan. Praesent id lectus quis mi molestie semper non sed felis. Etiam hendrerit risus eget malesuada suscipit. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis at lectus arcu. Nunc id augue sem. Mauris at augue urna.

Cras ut enim quis nunc feugiat lacinia quis a ex. Nullam eros nisl, accumsan at congue ut, facilisis at dolor. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Donec ullamcorper ligula eget egestas commodo. Pellentesque congue in ante vitae semper. Aliquam maximus lectus turpis, vitae vestibulum urna accumsan non. Suspendisse potenti. Cras vulputate enim tortor, eget cursus purus vehicula eget. Maecenas lacinia metus enim, sagittis porta eros sollicitudin in. Integer tincidunt augue sit amet mauris aliquam, sit amet scelerisque ex lacinia. Etiam condimentum orci in elit bibendum dapibus. Aenean leo ante, dignissim sagittis sodales at, tempor nec magna.

Nullam auctor accumsan lacus, sit amet tempor justo. Morbi ac nunc quis leo fermentum suscipit. Quisque tristique aliquet gravida. Cras vel libero tortor. Ut rutrum ullamcorper lacus at dignissim. Mauris bibendum dolor eu egestas semper. Proin id leo id lacus venenatis dignissim. Donec volutpat imperdiet magna, a malesuada mauris fermentum eget. Nam ullamcorper sapien leo, eu aliquam magna rhoncus et. Mauris elementum commodo leo eget volutpat. Integer sagittis neque eu massa scelerisque, eu consequat nibh hendrerit. Mauris lorem leo, dictum quis neque quis, gravida condimentum erat. Vivamus mauris augue, suscipit semper fringilla at, congue quis eros. Mauris a augue lacinia ipsum fermentum consequat. Mauris in est ac odio tempus ultricies.

Nullam non urna posuere, consequat massa ac, pellentesque nibh. Pellentesque molestie, justo vitae euismod ornare, augue nunc tempus enim, et ullamcorper libero risus a lacus. Cras gravida sodales augue in euismod. Suspendisse fringilla ipsum eget purus dapibus elementum. Aliquam quis lobortis diam. Duis in nisl et turpis faucibus tincidunt. Vestibulum tristique odio enim, ac tincidunt massa rhoncus a. Curabitur pulvinar hendrerit risus eu vehicula.

Morbi felis arcu, semper non augue et, gravida porta sem. Morbi fringilla urna vitae orci mollis dignissim. Suspendisse dictum tempus consequat. Pellentesque eu turpis at sem pharetra molestie. In nisi risus, eleifend sit amet ex sit amet, consequat tempus urna. Vestibulum suscipit vulputate elit id blandit. Proin sit amet dictum mi, posuere posuere mauris. Nunc accumsan, sapien eget mattis blandit, ex risus bibendum neque, vitae dictum libero dui quis nulla. Nulla scelerisque nibh id arcu posuere volutpat. Nulla iaculis pulvinar ultrices. Vestibulum sit amet rhoncus enim. Sed ultrices mi ut gravida vehicula. Sed ultrices, est non iaculis lacinia, eros nisl gravida neque, et scelerisque magna tortor quis nisl. Pellentesque non fermentum tortor.

Aliquam consequat est quis dolor lobortis, sit amet ultrices turpis ultricies. Vivamus lacinia ac dolor ut tincidunt. Praesent sed purus pharetra, imperdiet dui at, hendrerit risus. Sed et sapien in tellus viverra porta. Nunc elementum nisi ligula, ac congue est finibus at. Morbi id vulputate tortor, quis finibus mauris. Nulla facilisi. Integer vel vehicula arcu. Quisque nunc ipsum, placerat vitae tellus at, tristique laoreet magna.

Fusce in velit sollicitudin, suscipit eros quis, consectetur erat. Aliquam mollis lacinia tellus, quis bibendum tortor porttitor vel. Fusce ultrices luctus augue, ac blandit risus egestas a. Mauris tincidunt ligula a dui gravida aliquet. Nam quam tellus, ornare quis justo at, dignissim lacinia urna. Donec ac metus egestas, molestie nisi quis, vulputate nisi. Cras eu magna bibendum, porttitor purus ut, fringilla lorem. Quisque urna nunc, laoreet ac porta non, porttitor ut urna. Cras non suscipit elit, sed scelerisque metus.

Donec hendrerit nisi mauris, placerat malesuada sem egestas at. Etiam finibus ultricies turpis nec venenatis. Nulla ornare pretium nisi dapibus pretium. Morbi efficitur venenatis metus eget aliquam. Donec dolor diam, aliquam quis faucibus id, consequat ut tellus. Nam lobortis erat sed quam molestie, et scelerisque odio bibendum. Duis non placerat diam. Etiam rutrum nibh nec dolor lobortis pulvinar. Proin semper elit risus, et volutpat velit fringilla sit amet.

Praesent ultricies erat vel accumsan interdum. Donec semper justo et sem tristique pretium. Vestibulum tristique, tortor quis mattis laoreet, tortor ligula pretium dui, tempor viverra felis elit et justo. Suspendisse molestie metus eu tempus iaculis. In scelerisque sollicitudin nunc, consequat finibus tortor lacinia at. Suspendisse massa tellus, aliquam in diam nec, commodo mollis tellus. Nulla tincidunt ex eu ornare posuere. Aenean in velit arcu. Nullam eleifend mauris arcu, in hendrerit dolor gravida eu. Morbi hendrerit eu nisl eu aliquam. Donec sed velit laoreet, vehicula justo nec, ullamcorper enim.

Morbi nec lacinia nunc. Donec nec dolor euismod, blandit dui et, consequat mauris. Proin condimentum, massa euismod lobortis porta, dolor sem laoreet diam, egestas ultrices est nunc in dolor. Nam sed pellentesque lectus, in dignissim lectus. Quisque vitae fringilla odio. Vestibulum aliquam euismod facilisis. Nullam vitae tortor eget ex ornare egestas. Suspendisse ac erat enim. Donec aliquet sagittis purus ut accumsan. Fusce augue eros, viverra venenatis enim sagittis, ornare viverra tellus. Donec id nisi at metus laoreet mattis. Vivamus dignissim risus elit, ac sagittis turpis hendrerit ut. Integer finibus consequat interdum. Mauris dolor ligula, scelerisque sit amet dui sit amet, interdum ultrices magna. Vestibulum ac eleifend mauris, et finibus leo. Cras porttitor augue id nibh consectetur, vel dapibus risus aliquam.

Nulla a mattis leo, in ultrices massa. Donec non ultrices sapien, vitae suscipit nisi. Maecenas augue felis, accumsan lacinia augue nec, eleifend tincidunt leo. Maecenas faucibus augue et ligula suscipit, at porttitor leo mattis. Etiam sem odio, ullamcorper id tincidunt vel, eleifend eget sapien. Sed sed mi eu felis faucibus gravida. Nulla fermentum congue libero vitae luctus. Sed pharetra nec turpis sed ullamcorper. Duis at tellus consequat, vulputate quam sed, porttitor sapien.

Aliquam rutrum vel tortor ac interdum. Integer orci diam, condimentum non libero in, condimentum feugiat mauris. Nullam neque nisl, aliquet at mauris bibendum, vehicula varius urna. Donec aliquam aliquet leo, mollis tempus mauris porta ut. Ut rhoncus sagittis pharetra. Aenean suscipit ipsum at tincidunt blandit. Sed commodo dignissim elit a condimentum. Aliquam eu justo a mauris lacinia malesuada. Maecenas arcu nunc, gravida sed ante eget, dictum molestie ligula. Suspendisse nec dolor ultricies, pharetra magna in, interdum tortor. Nullam a nisi in mauris venenatis malesuada. Aliquam dapibus fermentum erat, non commodo massa malesuada at. Ut purus urna, auctor eget dui sed, dapibus tincidunt metus.

Morbi et congue odio, in facilisis velit. Aliquam porta odio eu erat imperdiet efficitur. Nunc et diam imperdiet, facilisis felis id, maximus augue. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Ut scelerisque varius consequat. Nulla facilisi. Donec dignissim metus in molestie elementum. Aliquam gravida diam a eleifend imperdiet. Morbi facilisis lacus vitae nunc tempor, a dictum diam placerat. Pellentesque congue mattis urna ac sagittis. Phasellus enim lorem, egestas blandit aliquam vel, sollicitudin blandit diam. Mauris eget ante justo. Donec commodo ex justo, id dictum mi pulvinar et. Ut condimentum finibus eros at molestie. In tempus feugiat pretium. Vestibulum ut dui risus.

Etiam non dolor eu felis lacinia finibus eu finibus nisi. Praesent justo orci, ullamcorper sit amet porta ac, tincidunt a magna. Ut neque ligula, fermentum et lobortis sit amet, feugiat quis arcu. In eu sodales orci, sit amet auctor libero. Praesent condimentum eros et lorem sodales fringilla. Sed volutpat, libero sed rutrum congue, ex lectus interdum diam, non venenatis augue est et justo. Quisque sit amet vestibulum magna. Sed id sapien pharetra, facilisis odio pulvinar, bibendum turpis. Morbi accumsan nibh in nisi facilisis, in consequat sem pellentesque. Suspendisse id ornare ante. Aenean bibendum vitae dui ac imperdiet. Nunc porttitor purus quis diam gravida rhoncus. Aenean in diam pharetra, bibendum mi non, vestibulum arcu. Quisque eu posuere enim. Donec vitae sapien ac sapien sollicitudin consequat sed vel urna. Donec dui lectus, blandit quis maximus ac, sollicitudin nec urna.

Nullam at malesuada quam. Curabitur gravida ac nisi in accumsan. Nullam odio orci, malesuada ut iaculis non, laoreet ac ex. In hac habitasse platea dictumst. Vestibulum aliquam nunc a ex tempor, eu iaculis tellus auctor. Donec id urna non velit laoreet suscipit. Curabitur hendrerit pharetra eleifend. Maecenas iaculis ipsum eu ornare vestibulum. Suspendisse nec velit placerat, dignissim ligula cursus, volutpat lorem. Nulla ultrices nisl quis faucibus congue. Proin odio neque, sollicitudin a mauris eget, posuere placerat augue. Nullam ultricies sed neque id aliquam. In mollis sed dolor vitae bibendum. Nullam luctus tempus bibendum. Mauris mattis ipsum eget felis semper, a placerat sapien feugiat.

Phasellus posuere est leo, vel blandit turpis varius sed. Duis luctus et ante et hendrerit. Etiam ipsum nisl, rhoncus at tempus at, elementum sed libero. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Donec nec volutpat elit. Fusce at tincidunt leo. Etiam purus libero, ultrices eu nulla sit amet, tristique finibus risus. Nulla ut ex pretium, tempor neque non, ullamcorper arcu. Duis ut nisi dolor. Vivamus ornare suscipit augue, sit amet feugiat metus mattis quis.

Integer rutrum augue sit amet magna vulputate posuere. Nam sed velit felis. Donec scelerisque, quam vitae lobortis varius, tortor purus rutrum ipsum, sit amet luctus dolor sem vitae elit. Maecenas ac justo a ipsum blandit semper et et augue. Aliquam mollis lacus mi, vel ullamcorper mauris porta vitae. Etiam viverra iaculis turpis. Integer pulvinar ornare ex finibus pulvinar. Etiam eleifend sodales enim ac mollis. Etiam id tempor enim. Vestibulum commodo consequat finibus.

Duis vestibulum libero ac varius faucibus. Sed eu fermentum tortor. Cras in consequat lorem, ac ullamcorper dui. Vivamus sed enim pellentesque, finibus augue quis, scelerisque dolor. Cras fringilla lorem sed purus condimentum tincidunt. Aliquam non aliquam leo, nec feugiat turpis. Donec malesuada neque tortor, eu sodales lectus volutpat ac. Vestibulum congue efficitur turpis non tincidunt. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos.

Nunc nisl augue, mattis elementum orci eget, porta interdum ligula. Mauris ante sem, posuere in scelerisque volutpat, egestas in mi. Curabitur sit amet elit in ipsum eleifend venenatis. Curabitur congue, diam eget sollicitudin ultrices, risus odio sodales lorem, quis ornare ipsum mi sit amet mauris. Sed diam velit, placerat ut diam lacinia, tempor venenatis libero. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Maecenas pretium leo risus, ac tincidunt velit bibendum at. Nullam quis ultricies ante. Curabitur ex orci, suscipit a sem id, blandit dapibus diam. Praesent luctus orci eget eleifend finibus. Sed tincidunt nulla a augue pellentesque, ut fermentum metus dignissim. Donec in tristique urna. Aliquam varius dignissim eleifend. Mauris ex augue, porttitor vitae nunc quis, rutrum interdum sem. Donec et turpis sit amet nisi posuere feugiat in nec arcu. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos.

Proin congue eget justo at sollicitudin. Sed sed vestibulum nunc, eget dignissim mauris. Nunc maximus magna eget arcu aliquet tempus. Suspendisse vulputate justo at maximus bibendum. Aenean lorem nunc, aliquet in ornare nec, dapibus vitae nulla. Sed interdum ultrices pretium. Praesent pharetra, orci vel laoreet hendrerit, purus eros sollicitudin urna, lacinia facilisis diam purus ac enim. Praesent mattis mauris in ex consectetur consectetur. Donec vitae vestibulum dolor. Duis enim urna, pellentesque tristique leo at, porta ultricies arcu. Curabitur eleifend dolor vel condimentum pretium. Fusce tortor nisi, faucibus in efficitur sit amet, sollicitudin id neque. Nulla nunc dolor, accumsan id suscipit nec, finibus in justo. Ut nec nunc ac elit fermentum suscipit. Proin vehicula purus erat, sit amet finibus ante elementum at. Cras dui orci, scelerisque eu lectus at, congue faucibus justo.

Nulla non euismod sem, quis consectetur nisl. Proin condimentum sollicitudin laoreet. Ut et commodo nibh. Morbi vel sapien vitae turpis cursus blandit. Nulla facilisi. Praesent imperdiet turpis a condimentum pulvinar. Donec lacus leo, ultricies quis bibendum nec, suscipit in purus. Cras nec dui auctor, aliquam lorem porttitor, porttitor purus. Suspendisse condimentum euismod mauris. Cras ut vehicula lorem. Phasellus ac blandit metus. Suspendisse consequat lacinia tristique. Pellentesque finibus condimentum sapien a ornare. Aenean maximus augue sit amet nunc interdum, non efficitur lectus commodo.

Aenean tortor urna, tincidunt nec condimentum sit amet, dignissim quis mi. Proin pulvinar ullamcorper lorem eu feugiat. Aliquam pharetra sodales metus, nec egestas nisl sagittis a. Donec vitae semper nulla. Curabitur lacus dui, aliquam at diam bibendum, aliquet rhoncus nibh. Maecenas rhoncus est accumsan enim volutpat finibus. Quisque scelerisque metus mi, ac suscipit urna volutpat eget. Maecenas eu tempus quam. Sed quis fermentum orci. Duis vitae egestas orci. Nam gravida in leo vel molestie.

Ut consectetur nisi urna, quis mattis metus congue eget. Duis ac arcu tincidunt, faucibus sem ultrices, egestas tellus. Aliquam ullamcorper dui vitae lorem iaculis malesuada. Quisque fermentum purus non tortor pellentesque, a fermentum risus pulvinar. Pellentesque feugiat et mi vel lacinia. Morbi non blandit nisi. Mauris vel imperdiet est. Proin tempus quam non lectus ultrices, sed placerat quam feugiat. Nullam ullamcorper eleifend mi, et accumsan libero accumsan quis. Vestibulum luctus arcu vel mauris aliquam blandit.

Phasellus eget ultricies turpis. Maecenas maximus eros id convallis rhoncus. Nam justo risus, tempor ac elit ut, dictum bibendum urna. Mauris vitae rhoncus nulla, nec vulputate turpis. Vestibulum placerat accumsan magna, quis dapibus diam. Fusce scelerisque pretium diam, et cursus mauris convallis sed. Vivamus eu tellus quis enim accumsan condimentum.

Quisque tempus congue ex elementum mattis. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Suspendisse et sodales eros. Nunc eget aliquet dolor. Nullam viverra magna leo, sed porta massa elementum eu. Mauris massa urna, iaculis eu urna id, vehicula placerat neque. Sed euismod mi ut dolor mollis, vitae sollicitudin leo maximus. Donec ut tortor rhoncus, gravida ante quis, bibendum ligula. Nullam fringilla elit quis lectus mattis, rhoncus iaculis tortor luctus. Nunc non neque maximus, malesuada felis sit amet, sollicitudin ipsum. Vivamus ut massa vel urna vulputate consequat. Fusce ac malesuada justo.

Sed facilisis nibh eu lacus lobortis sollicitudin non vel purus. Integer vitae turpis convallis, efficitur turpis eu, aliquet justo. Sed purus lacus, facilisis sit amet dolor non, rutrum cursus libero. Nam lobortis pulvinar mauris at viverra. Quisque porta augue urna, a scelerisque est finibus sed. Fusce nec risus finibus, accumsan risus at, feugiat sem. Etiam fringilla erat fringilla nunc vehicula, id suscipit enim placerat. Aliquam erat volutpat. Sed tincidunt est id est pellentesque, eu faucibus felis eleifend. Ut leo libero, suscipit in neque in, interdum tincidunt ipsum. Fusce quis neque eu velit sodales dignissim nec non magna. Fusce commodo viverra pretium. Donec vulputate malesuada feugiat. Aliquam odio sem, tempor at augue nec, porttitor tempor lectus. Mauris nisi turpis, laoreet eget odio sed, pharetra fringilla quam.

Sed pharetra mi sed libero maximus condimentum. Praesent a dui ante. Aliquam purus nulla, semper id mi eu, sodales bibendum purus. Aenean eget vestibulum justo, pellentesque ultricies nisl. Nulla quis odio magna. Sed nec lorem rhoncus, pellentesque magna quis, euismod massa. Donec tellus turpis, ornare quis quam quis, dapibus varius risus. In auctor tempus efficitur. Aenean commodo lectus pretium, tristique tortor a, blandit sapien. Donec nec erat at nisi semper convallis. Etiam mollis est id fringilla efficitur. Etiam sed lacus eget urna pharetra semper. Nullam luctus arcu massa, eu placerat risus euismod vitae. Morbi ut ullamcorper erat. Etiam felis augue, varius eu risus facilisis, tempus mattis risus.

Duis augue nunc, vulputate eget turpis eleifend, malesuada ultrices mauris. Curabitur sit amet lorem pulvinar, varius mauris vitae, ullamcorper tortor. Sed dolor magna, aliquet in finibus eget, elementum nec dolor. Nam auctor ante at elit ullamcorper, nec accumsan dolor gravida. Donec condimentum volutpat dapibus. Ut in nulla eu lectus commodo aliquam ut nec augue. Ut volutpat urna quis ultricies tristique. Nulla non rutrum orci.

Sed sagittis porttitor gravida. Pellentesque tempus libero ut arcu finibus luctus. Nulla gravida, nisi et facilisis feugiat, lectus lectus mollis urna, at laoreet nunc mauris vitae odio. Sed vel pretium lacus, in aliquam ligula. Vestibulum lacinia lorem et diam fringilla hendrerit non a mauris. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Aenean hendrerit arcu ut nisi commodo, vel suscipit ante consequat. Pellentesque a libero augue.

Pellentesque ut aliquam neque. Vivamus commodo quam id bibendum convallis. In sed efficitur mi, quis tincidunt justo. Pellentesque id varius ante. Fusce nibh justo, dignissim at ligula porta, molestie rhoncus odio. Proin justo sapien, fermentum at commodo in, vehicula vitae mi. Duis eleifend, libero tincidunt maximus maximus, ante leo ultricies metus, sit amet sollicitudin neque quam eget mi. Curabitur sagittis hendrerit posuere. Aliquam euismod, mauris quis fermentum varius, dui neque aliquet arcu, vel sollicitudin ipsum libero vitae nisi. Praesent mi felis, sollicitudin vitae eros ut, pulvinar laoreet elit. Donec aliquam ipsum ut diam vehicula, et ultrices sem tristique. Phasellus quis imperdiet nisi. Suspendisse interdum dignissim massa consequat pellentesque. Maecenas sed nulla viverra, ullamcorper enim ut, vehicula nisi.

Donec lacus magna, commodo at dapibus eget, tincidunt ut arcu. Vestibulum dolor odio, efficitur in porta vel, aliquam et libero. In eget ex sodales, euismod libero ac, sollicitudin nisl. In ac rhoncus ante. Duis enim felis, vulputate ac tempor sed, gravida at nisl. Mauris vitae nulla pretium, convallis massa vitae, euismod leo. Nullam auctor, nibh sed suscipit imperdiet, orci magna semper massa, vel dignissim diam ex sit amet mi. Curabitur et laoreet erat, id bibendum tortor.

Quisque ultrices mauris eget dolor lobortis mollis. Etiam tempor congue metus, nec luctus urna porta a. Donec id accumsan ipsum, et finibus sem. Duis lacinia ullamcorper mauris, eu venenatis risus vulputate quis. Proin vehicula fermentum sapien sed eleifend. Donec vel lacus euismod, vestibulum libero pretium, scelerisque dolor. Integer efficitur egestas tortor id porttitor. Praesent at ipsum congue, commodo ipsum at, semper felis. Sed in libero eu diam varius pulvinar rhoncus sed tellus.

Pellentesque quam risus, faucibus nec semper vel, rutrum ut mauris. Nam faucibus, elit quis luctus maximus, mi tortor porttitor mauris, a tristique massa metus in massa. Cras sed efficitur justo, sit amet sodales dolor. Vestibulum ut sagittis enim. Nulla facilisi. In eu varius velit, non interdum odio. Maecenas hendrerit leo ac dignissim maximus. Phasellus pretium elit vitae tellus scelerisque semper. Integer lacinia semper blandit. Etiam magna magna, auctor vitae nibh pellentesque, porttitor blandit nisl. In hac habitasse platea dictumst. Mauris feugiat venenatis turpis in gravida. Morbi eu augue commodo, venenatis purus eu, convallis ligula. Vestibulum hendrerit, massa ac dapibus vestibulum, lorem metus ornare mi, ac dignissim augue diam et nibh. Proin blandit elit ut nisi blandit, eu suscipit enim tempus.

Etiam tempus, elit ac sagittis tristique, elit eros feugiat lorem, in dignissim mauris ante in leo. Quisque aliquet diam sed blandit porta. Sed ultrices ligula sit amet finibus congue. Integer eu iaculis dui. Proin venenatis tempor metus quis maximus. In ultrices mi nec vulputate varius. Nunc condimentum consequat nisl at tincidunt. Nunc at nulla commodo, efficitur lectus ac, sagittis diam. Integer vel aliquet nibh. In tempor commodo ultrices.

Suspendisse vel sapien commodo, feugiat mi id, vulputate elit. In porttitor erat auctor magna pharetra pulvinar. Vivamus ut urna at enim sodales dictum. In sollicitudin, ligula id finibus egestas, quam diam ornare tellus, nec semper tellus libero a tortor. Phasellus lorem lorem, scelerisque sit amet ullamcorper ac, pellentesque ut justo. Phasellus neque arcu, volutpat et varius non, rutrum ut mi. Proin ultricies ipsum ac tellus imperdiet, sed luctus enim venenatis. Mauris lobortis justo a laoreet auctor. Sed est elit, suscipit in massa ac, tristique molestie erat. Sed euismod placerat justo, in laoreet massa suscipit a. Vestibulum porttitor dui rhoncus massa tempus, sed semper est feugiat.

Etiam sagittis mattis porttitor. Cras nec nisi in tortor dapibus tincidunt vel in metus. Interdum et malesuada fames ac ante ipsum primis in faucibus. Mauris a bibendum ante, ac pellentesque est. Morbi finibus enim neque, id mattis augue viverra id. Sed suscipit dolor purus, sit amet pellentesque nulla viverra eu. Donec ut dictum risus. Praesent efficitur sem ac lacus scelerisque, quis ultrices enim fringilla. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Aliquam blandit tortor vel nisi porttitor, eu egestas tellus tempor. Ut eget aliquet nisl. Vivamus ornare luctus congue. Nullam in nisi nisl.

Phasellus placerat et purus et facilisis. Integer pulvinar venenatis nulla, nec sollicitudin quam gravida eget. Pellentesque accumsan fringilla gravida. Fusce nec eros ut ex suscipit finibus. Aliquam in scelerisque enim. Donec vel massa maximus, suscipit est in, elementum orci. Mauris sed urna congue, commodo nisi quis, convallis elit. Quisque tortor eros, sodales et quam quis, ullamcorper efficitur nunc. Aliquam consequat odio ac eros condimentum, eget rhoncus odio pulvinar. Sed pellentesque purus id tellus facilisis feugiat. Aliquam vitae auctor nunc. Donec vitae nunc scelerisque ex viverra porttitor. Lorem ipsum dolor sit amet, consectetur adipiscing elit.

Etiam vel dolor a enim lacinia mattis in eu est. Fusce rutrum metus lacus, nec elementum ligula consectetur eget. Fusce non felis a felis venenatis interdum. Aliquam posuere, quam et dictum facilisis, ligula sapien commodo velit, varius volutpat justo quam non urna. Donec ultrices urna a est volutpat tincidunt. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed sit amet elementum augue. Vivamus molestie auctor lorem consectetur viverra. Vestibulum quis finibus libero, eget tempor mi. Sed auctor luctus metus, vitae commodo erat porttitor at. Morbi finibus molestie tortor, quis porttitor augue sodales ut. Vivamus sed ornare justo. Vestibulum non posuere ligula. In imperdiet erat et odio ultrices, at tincidunt ligula posuere. Vestibulum non posuere lectus.

Sed commodo consequat diam, sit amet molestie nulla. Proin maximus congue nunc eget dictum. In vitae sapien mi. Pellentesque iaculis eget dui sit amet maximus. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent gravida vestibulum quam, malesuada tempor est scelerisque placerat. In eu odio pharetra ligula faucibus molestie. Cras lobortis massa eu sem tempor, eget suscipit urna sagittis. Etiam eu fermentum risus. Sed efficitur vitae ligula et tempus.

Nam bibendum scelerisque urna eu finibus. Vivamus cursus rutrum maximus. In semper ex ex, a placerat ante pharetra at. Mauris aliquam enim ut semper imperdiet. In interdum enim vel dolor aliquam rutrum. Fusce mauris ante, maximus ut laoreet et, semper at urna. Vivamus a nisl at tellus malesuada facilisis. Duis ac dolor non felis eleifend eleifend. Donec viverra metus id diam posuere eleifend.

Phasellus quis diam magna. Nullam nec vehicula est. Nam sit amet elementum nunc. In fringilla dolor vel malesuada cursus. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris dignissim convallis diam, et tincidunt arcu vestibulum id. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Nam urna elit, molestie egestas augue quis, dictum malesuada nibh. Cras nec tempus dui. Aenean a posuere tellus. Maecenas egestas facilisis turpis, in viverra massa ullamcorper at. Nam lacus lectus, lacinia ut consectetur ac, lacinia in turpis. Mauris id ipsum vitae lectus semper auctor sit amet eu lacus. Donec quam dui, auctor in elit ut, commodo luctus dolor.

Aenean ante leo, rutrum blandit consectetur eget, ullamcorper sed risus. Mauris quis velit maximus, imperdiet sem eget, dictum velit. Curabitur sit amet pellentesque nibh, vitae laoreet neque. Duis venenatis odio suscipit, placerat lectus quis, bibendum mi. Curabitur elit ipsum, varius id mauris volutpat, accumsan efficitur est. Proin condimentum rhoncus maximus. Sed fermentum, massa sed mollis fringilla, tortor augue venenatis justo, a condimentum lectus eros ac dui. In laoreet neque leo, sed tincidunt ex commodo in. Donec eu vestibulum nulla, eget efficitur nulla. Fusce tempus arcu nec justo rutrum interdum. Nulla eu porta sem. Cras ut volutpat velit. Integer feugiat sollicitudin dolor, ut commodo nisl sollicitudin fermentum. Etiam placerat leo ex, in porttitor velit congue in. Praesent et blandit orci. Cras molestie mollis augue sit amet tincidunt.

Vestibulum eleifend maximus velit, et finibus lectus malesuada ut. Quisque justo magna, efficitur a euismod et, placerat eu metus. Praesent eu ipsum lorem. In vitae purus rhoncus, fermentum nibh quis, viverra metus. Duis non commodo magna. Praesent vulputate tellus sed justo ornare, a molestie ante viverra. Praesent sollicitudin sollicitudin iaculis. Duis ac nibh arcu. Vivamus euismod, turpis sit amet imperdiet eleifend, elit lacus eleifend dui, non tempus tellus arcu non elit. Mauris sit amet lorem cursus, elementum urna eu, sollicitudin nunc. In sed finibus purus. Nunc pulvinar purus sed magna fermentum sodales. Nunc volutpat sem ut pretium rhoncus. Quisque vulputate ac dolor ac vestibulum. Quisque euismod nulla vitae ligula ultricies, id bibendum lacus malesuada.

Maecenas varius, augue nec vehicula auctor, augue nulla efficitur enim, vitae lacinia quam justo vel velit. Nulla eget pharetra risus. Nunc ac auctor lectus. In ultricies malesuada bibendum. Etiam pellentesque elit diam, sit amet rhoncus magna sollicitudin et. Cras euismod eu purus at faucibus. Phasellus ut orci ac arcu eleifend hendrerit porttitor in ligula. Aliquam vitae enim neque. Ut in feugiat est, in viverra metus. Pellentesque eleifend, diam quis sollicitudin congue, sapien leo porta arcu, et sollicitudin enim diam quis sapien. Integer vel porta erat. Fusce ultricies consequat tellus, eget tempor tortor elementum vel. Cras volutpat, ante eu tincidunt egestas, diam arcu scelerisque justo, in vulputate dolor ante vel velit. Integer vitae condimentum risus. Mauris eu orci tincidunt, pellentesque nulla a, lacinia nunc.

Sed ut pharetra lacus. Suspendisse congue, nibh cursus tincidunt sodales, risus diam facilisis metus, eget tristique dolor risus id risus. Praesent elementum augue vitae ligula imperdiet, sit amet malesuada massa scelerisque. Nullam at ullamcorper enim, non condimentum erat. Donec vel purus molestie, scelerisque metus non, efficitur nunc. Fusce quis tellus eros. Integer eu porta sem, ut sagittis ligula. Donec rhoncus pretium magna vitae pretium. Nullam tellus libero, dapibus quis tincidunt a, pellentesque in odio. Phasellus malesuada erat ut facilisis malesuada. Fusce dapibus finibus sem, id gravida sapien vulputate sit amet. Nullam id consequat elit, ut tempor est. Aenean leo turpis, aliquam in lectus sed, vulputate vestibulum turpis.

Integer vel cursus magna. Aenean venenatis nisl at nisl posuere, molestie venenatis ligula varius. Suspendisse quis mi vitae mauris feugiat aliquet. Nullam at ultricies enim, vitae ultrices metus. Sed quis purus eu turpis commodo placerat sed vel magna. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Morbi interdum condimentum nunc, id condimentum urna hendrerit ac. Quisque pharetra quis elit sed dapibus. Fusce quis rhoncus sapien. Ut placerat sapien purus, sed finibus justo fermentum sed. Morbi mollis, ante aliquam mattis fermentum, tellus sapien egestas ligula, at porta enim arcu et nulla. Sed eros massa, semper in lorem nec, consectetur ornare velit. Fusce placerat, magna non ornare mollis, metus felis aliquet mauris, cursus efficitur eros velit eget purus.

Vivamus porta purus non cursus commodo. Nam efficitur feugiat vehicula. Donec accumsan felis eu pulvinar dignissim. Pellentesque risus tortor, pellentesque et felis eu, aliquam posuere lacus. Vivamus tempus ut libero vel pellentesque. Nunc elementum nisl ut interdum euismod. Donec urna sem, pulvinar sed lorem nec, interdum pulvinar erat. Nunc elementum venenatis lectus, vitae varius elit sagittis a. Vivamus ac vulputate massa. Nulla tristique ante et euismod pharetra.

Phasellus porta diam dui, sed sodales ante sollicitudin ac. Fusce dignissim fermentum orci id mattis. Mauris risus tellus, viverra sit amet tempus id, egestas sit amet diam. Suspendisse vitae dapibus nunc, nec vehicula tellus. Vivamus fringilla nibh et dignissim euismod. Vivamus tempor est eu viverra ullamcorper. Cras augue risus, posuere ut metus vitae, faucibus tincidunt velit. Vivamus consequat justo eu est posuere, ut consectetur metus dignissim. Praesent odio nibh, posuere ut rutrum lobortis, luctus consectetur tortor.

Integer vel enim ut metus hendrerit fringilla. Fusce scelerisque bibendum lorem, euismod dignissim metus efficitur congue. Pellentesque in sapien ornare, aliquet nunc sagittis, pulvinar mi. Aenean eleifend consequat odio, at faucibus nisl laoreet semper. Proin placerat pellentesque arcu ac fringilla. Sed placerat eleifend dui, in molestie magna consequat eget. Pellentesque et accumsan magna, vel bibendum mauris. Sed lobortis nisi at egestas fermentum. Donec porta urna ut erat cursus congue.

Mauris ut mattis mauris. Aliquam vitae augue nulla. Integer dapibus vel ipsum id malesuada. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Integer quis elementum justo. Nullam ac velit eu tellus varius tempor. Maecenas fermentum tortor ex, sit amet mattis quam semper nec. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Nam sed diam feugiat, sagittis ligula in, egestas arcu. Mauris in dignissim eros. Nulla mi dui, ullamcorper id molestie nec, lobortis vel nisi. Maecenas quis commodo lorem, et facilisis mauris. Nam leo quam, posuere in hendrerit semper, congue ut mauris. Cras nisi sapien, commodo eu faucibus vel, rhoncus et ipsum. Quisque eu tortor sit amet libero pretium tincidunt.

Morbi ante tortor, suscipit vitae egestas at, eleifend ac nulla. Sed semper massa ut enim tincidunt maximus. In venenatis, enim eu finibus viverra, ante dolor ullamcorper eros, vitae cursus neque tortor id velit. Aliquam convallis arcu sed tortor luctus, et elementum velit pretium. Phasellus pharetra leo quis tellus rutrum dapibus. Donec sit amet quam urna. Mauris erat augue, suscipit id placerat non, semper ut arcu. Nam consequat, quam non commodo pellentesque, dui tortor cursus lectus, ac lacinia nisl purus sed nibh. Ut non ipsum vehicula, euismod mi eget, dictum justo. Nullam eu nibh ex. Maecenas aliquet at lectus nec porttitor. Aliquam erat volutpat.

Suspendisse euismod aliquet est. Etiam eu sem fermentum, consectetur nulla id, aliquet dolor. Vivamus vehicula tristique augue ac cursus. Morbi blandit in lacus in condimentum. Aliquam quis orci a urna sagittis dignissim. Sed id finibus risus, sit amet lacinia ligula. Integer vitae neque odio. Nullam malesuada, dui eu cursus maximus, erat ex egestas ligula, eu fermentum tortor tortor vitae nulla. Interdum et malesuada fames ac ante ipsum primis in faucibus. Proin ac mattis massa. Nulla ullamcorper mauris et arcu molestie finibus. Pellentesque commodo bibendum mauris, elementum fringilla mi finibus eu. Donec sed diam ornare nibh convallis efficitur sodales eget ligula. Aliquam sed felis vel leo dictum accumsan. Nunc quis placerat ligula. Curabitur dapibus sed justo in luctus.

Fusce tincidunt, felis faucibus convallis accumsan, velit augue rutrum lectus, in pharetra nulla sapien eget purus. Proin pulvinar at ex non viverra. Praesent in mauris tortor. Nunc est dolor, varius placerat sapien at, vulputate ornare erat. Vivamus sit amet orci eu lectus sollicitudin finibus nec volutpat dolor. In hac habitasse platea dictumst. Vivamus nulla dolor, blandit ac tincidunt eu, fringilla a orci. Suspendisse potenti. Etiam id sollicitudin leo, quis egestas lectus. Vivamus vitae mi tempus, venenatis nisl id, pellentesque mauris. Quisque dapibus mi eget sem scelerisque, quis ullamcorper mi mattis. Etiam pretium dolor quis tempus mollis. Phasellus non pretium nulla, sed molestie erat.

Sed commodo pellentesque ante, quis ullamcorper mauris condimentum eu. Praesent ac venenatis ligula, sed volutpat lorem. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Aenean congue nunc diam, vel ultrices erat vulputate nec. Maecenas sit amet felis placerat, mollis lectus quis, tincidunt mauris. Donec nec arcu molestie, congue libero ut, vestibulum leo. Morbi ultrices porttitor nisi eget ultrices. Nulla dictum lectus sit amet tellus sodales porttitor.

Duis at fermentum dui, dignissim interdum risus. Donec sed aliquam urna. Quisque ut suscipit justo. Pellentesque et diam odio. Duis semper rhoncus leo eu accumsan. Sed justo nunc, egestas ac posuere in, lobortis id dui. Sed finibus arcu vitae massa maximus gravida. Nam in tincidunt est. Vivamus quis vestibulum nibh, sed euismod ligula. Donec pellentesque posuere velit vel rhoncus. Nulla convallis ante in mattis convallis. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum sagittis commodo viverra. Donec blandit, dolor vitae pellentesque aliquam, velit purus convallis urna, ut auctor felis mi sed dui. Nullam vitae lacus vel tortor blandit tincidunt.

Mauris eu nisl quis urna dictum gravida. Ut at dictum nunc. Donec malesuada porta auctor. Fusce nibh neque, finibus sed malesuada ut, pellentesque id lorem. Pellentesque luctus id nisl sed tincidunt. Praesent interdum mattis dolor a mattis. Suspendisse in viverra metus. Cras laoreet lacus vel tellus facilisis mattis. Sed interdum, magna ac fringilla pulvinar, ligula enim ornare leo, eu cursus purus turpis vel nibh. Suspendisse efficitur metus eu placerat porttitor.

Proin euismod semper eleifend. Sed et metus facilisis, gravida nibh in, molestie turpis. Sed et ex sollicitudin, maximus augue vitae, pharetra nulla. Donec eros tortor, gravida in felis nec, mollis pellentesque est. Maecenas hendrerit, mi eu porttitor dapibus, magna elit consequat dui, sed dignissim massa risus eu odio. Proin venenatis bibendum felis non semper. Mauris rhoncus laoreet mauris. Integer elementum efficitur risus eu ultricies. Quisque a tincidunt elit. Fusce lacinia eget libero ut dignissim. Nunc venenatis malesuada massa, quis dictum nisl mollis in. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Maecenas est nulla, placerat in ex eget, dignissim interdum leo. Phasellus vulputate eros vitae tempor pretium. Etiam at tincidunt odio, nec rhoncus nisl.

Cras vulputate, orci nec vestibulum auctor, eros massa consequat felis, eu lobortis ipsum dolor sit amet lacus. Aliquam nec magna vel purus luctus malesuada. Integer velit metus, posuere eu lacinia vel, bibendum eget libero. Sed at tempor orci. Etiam et suscipit ipsum, hendrerit sagittis urna. Donec a aliquet ipsum. Cras sollicitudin ipsum a viverra ultricies. Cras id nunc est.

Suspendisse potenti. Cras non finibus ipsum. Proin eros orci, scelerisque et sapien et, convallis bibendum lacus. Aenean vehicula eu felis non commodo. Sed eget justo volutpat, hendrerit velit ut, tristique turpis. Etiam quis quam convallis, dapibus libero accumsan, efficitur nibh. Maecenas ex massa, sollicitudin et vehicula a, viverra eget diam. Donec feugiat leo eget erat dignissim fringilla.

Suspendisse et odio felis. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Duis vehicula ante ac convallis dictum. Etiam quis ipsum non lorem imperdiet porta. Pellentesque vitae est ac justo pharetra dignissim congue et elit. In lacinia enim purus, sed maximus tortor porttitor quis. Vestibulum rhoncus purus ut mauris porta, at dignissim arcu dignissim. Suspendisse at augue orci. Nam ornare dui et felis tempor, eget dictum est sagittis. In fermentum vel velit quis accumsan. Nam tempus rutrum risus sed lacinia. Phasellus congue sem id diam viverra, vitae feugiat ligula tincidunt. Curabitur ornare eros et mi mollis interdum. Curabitur in eros urna. Fusce dignissim nisi nisi, eu ullamcorper mauris viverra pulvinar.

Vivamus aliquet elementum diam. Nam a turpis dictum, tristique velit eu, malesuada ipsum. Ut consectetur purus vel facilisis scelerisque. Duis vulputate magna arcu, quis malesuada metus euismod non. Ut dui neque, lobortis eu congue ac, aliquet quis nunc. Nunc id nisl neque. Pellentesque odio arcu, consequat eu elementum sed, sagittis id quam.

Nunc ligula lectus, scelerisque pulvinar nibh et, interdum dapibus dolor. In convallis mi eros, a volutpat nibh maximus ut. Cras sagittis sapien ac augue finibus pharetra. Nullam id magna nulla. Donec pretium nisl lacinia mi fringilla, id malesuada nunc dictum. In aliquam ligula ante, ac molestie nisl accumsan sit amet. Aliquam laoreet felis velit, ut vulputate odio congue et. Proin quis arcu fringilla, condimentum lectus non, luctus sem. Etiam tempor quam non neque fermentum sollicitudin. Ut dolor orci, feugiat ut turpis sit amet, porta ornare lectus. Donec in nibh vitae nibh maximus consectetur in sed risus. Quisque blandit dictum pellentesque.

Nullam lacinia efficitur urna, vitae luctus odio lobortis vel. Nullam sodales, felis ut luctus ullamcorper, nulla urna pharetra elit, eu sagittis diam risus sed mauris. Mauris imperdiet ultricies elit, malesuada posuere purus dapibus in. Phasellus ligula magna, semper ac elit nec, placerat scelerisque lacus. Pellentesque faucibus rutrum aliquet. Interdum et malesuada fames ac ante ipsum primis in faucibus. Donec in ipsum dolor. Mauris gravida sapien quis ex lobortis sodales. Integer a posuere libero, vel consectetur ligula.

Phasellus vel porta lacus. In a eros eu diam feugiat luctus et non diam. Quisque cursus lacus nec est egestas, at consectetur nisi bibendum. Etiam ultricies orci sed blandit fringilla. Integer porta dapibus nulla, id bibendum purus elementum ut. Proin nec diam efficitur, volutpat leo eget, pharetra nisi. Fusce in enim quis neque tincidunt aliquet eu vitae purus. Cras efficitur facilisis cursus. Donec eu turpis et arcu volutpat vehicula eu et tortor. Integer bibendum ligula eu maximus eleifend.

Sed eget bibendum elit, eu ullamcorper metus. Phasellus sit amet purus vel urna blandit dignissim sed eu velit. Maecenas facilisis urna sit amet est pretium, faucibus viverra nibh commodo. Duis sagittis, ex in vulputate ullamcorper, velit arcu maximus libero, ac bibendum ligula arcu et purus. Vivamus at mauris sed magna varius dapibus at sit amet lacus. Sed risus ligula, lobortis quis egestas sit amet, rhoncus a lorem. Duis lacus orci, ullamcorper non rhoncus sed, blandit ac eros.

Nam ut auctor velit, nec mollis felis. Nulla facilisis orci eleifend, lobortis ante sed, mattis diam. Nullam mattis dignissim risus, vitae tincidunt ante tempus eget. Phasellus commodo scelerisque purus, vel iaculis ligula interdum id. Pellentesque pulvinar viverra semper. Praesent sed velit mauris. Mauris a dolor sed tellus mollis semper at in tortor. Sed at ultrices eros. Vestibulum purus dui, commodo in mauris in, auctor aliquam turpis. Ut vitae orci vitae magna ultrices commodo. Ut nec libero et lorem hendrerit dignissim. Duis iaculis ante diam, vitae pretium justo scelerisque id. Nullam gravida aliquet mi eget malesuada. Proin nec pellentesque libero, accumsan scelerisque leo. Vivamus posuere varius tortor in fringilla. Nulla facilisi.

Duis eu orci ac justo sagittis porttitor. Nullam interdum erat ante, vel tristique lorem rutrum at. Morbi ac scelerisque massa, ornare rutrum dui. Duis aliquet quam ac eros tincidunt, ut scelerisque metus hendrerit. Integer in eleifend nulla. Mauris congue leo laoreet nisl vulputate, sed placerat leo rutrum. Vestibulum rhoncus magna mi, at bibendum dolor lacinia eu. Proin in neque id nisi ullamcorper euismod. Nullam rhoncus elementum tellus aliquet semper. Donec pharetra nisl a elit sagittis rutrum. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Nam bibendum ipsum imperdiet laoreet interdum. Duis malesuada id mi nec auctor.

Curabitur at consectetur magna. Ut gravida tincidunt ex eget aliquam. In a aliquet erat, a euismod nulla. Nam vel faucibus libero. Morbi ullamcorper, velit non hendrerit pretium, nisi mi maximus enim, a tristique mi nibh eu quam. Aenean rhoncus diam ut velit tincidunt feugiat. Nunc ut arcu a tellus scelerisque vehicula. Duis sollicitudin cursus commodo.

Praesent ut nisl nisi. Maecenas bibendum ex a ex mattis euismod. Phasellus lobortis nisi in elit rutrum ornare. Pellentesque mollis, sem ac condimentum malesuada, purus purus blandit elit, at interdum orci erat vel leo. Donec ut sagittis arcu. Curabitur ornare, magna non varius lobortis, est arcu posuere sem, quis tincidunt urna augue nec orci. Nunc risus ante, pulvinar accumsan convallis non, semper sollicitudin nunc. Vivamus imperdiet risus ex, id fermentum odio commodo ut.

Etiam venenatis elementum ante, sed posuere magna pretium id. Maecenas id nunc vel velit accumsan molestie. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Proin finibus mi ac tempus tempor. Sed bibendum, neque sed cursus sollicitudin, metus arcu lobortis mi, non viverra nunc augue non odio. Nulla non convallis risus. Curabitur tempor felis tortor, vel pulvinar justo suscipit at. Proin lacinia orci aliquet leo pulvinar blandit. Aenean at vulputate nunc, id sagittis libero. Ut purus tellus, ultricies gravida posuere quis, commodo eget metus. Vestibulum quis rutrum nisl. Nulla id ornare lectus, fringilla laoreet nulla. Aenean mattis tincidunt libero sit amet bibendum. Nulla ut justo eget nunc ultrices convallis. Cras semper, erat at dapibus efficitur, elit justo auctor nisl, sed sodales orci odio quis lacus. Vestibulum nec facilisis arcu.

Cras mollis mi non libero placerat rhoncus. Nam felis arcu, gravida et vulputate in, iaculis at dui. Suspendisse eget elit justo. Cras suscipit pellentesque lectus, blandit ullamcorper nulla lobortis a. Phasellus luctus turpis sit amet enim malesuada, ac condimentum massa sodales. Donec porttitor elit quis felis fringilla consectetur. In in pulvinar ligula.
""";

    assertThatThrownBy(() -> wordOccurrence(StringUtils.EMPTY, "Lorem"))
        .isExactlyInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> wordOccurrence(text, StringUtils.EMPTY))
        .isExactlyInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> wordOccurrence(null, "Lorem"))
        .isExactlyInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> wordOccurrence(text, null))
        .isExactlyInstanceOf(IllegalArgumentException.class);
    assertThat(wordOccurrence(text, "Hello")).isZero();
    assertThat(wordOccurrence(text, "Lorem")).isEqualTo(3);
    assertThat(wordOccurrence(text2, "Lorem")).isEqualTo(2);
    assertThat(wordOccurrence(text3,"lorem")).isEqualTo(59);
    sp.stop();
    log.info("Test duration is {} (millis) ", sp.getDuration().toMillis());
  }

  /**
   * Instead build a new wheel use an existing one, you may forget same cases
   *
   * @param text The input text
   * @param word The word occurrences to calc
   * @return The occurrences found
   */
  private int wordOccurrence(String text, String word) {

    if (StringUtils.isBlank(text)) {
      throw new IllegalArgumentException("Text is empty");
    }
    if (StringUtils.isBlank(word)) {
      throw new IllegalArgumentException("Word to check is empty");
    }
    var occurrence = new AtomicInteger();
    text.lines()
        .forEach(
            line -> {
              var counts = StringUtils.countMatches(line.toLowerCase(), word.toLowerCase());
              if (counts > 0) {
                occurrence.addAndGet(counts);
              }
            });
    return occurrence.get();
  }
}
