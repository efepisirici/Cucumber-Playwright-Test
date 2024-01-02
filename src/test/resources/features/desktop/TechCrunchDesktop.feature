Feature: TechCrunch Article Verification

  Background:
    Given I load the page with "Chromium" for "desktop" usage
    And I navigate to the TechCrunch website

  @Case1 @Case2
  Scenario: Verify each article in the TechCrunch has an author
    Then each featured article should have an author
    Then each latest article should have an author

  @Case1 @Case2
  Scenario: Verify old article represented perfectly
    When I click on Load More Button
    Then each featured article should have an author
    Then each latest article should have an author

  @Case1 @Case2
  Scenario: Verify each article has images
    Then main featured article should have an image
    Then each latest article should have an image

  @Case3
  Scenario: Verify latest article
    Then navigate to latest article

