# Amazon Comprehend Bias Test
An implementation that checks bias in Amazon's Comprehend service on the attributes of gender and race. This is an informal study on how the inherent biases of those who create and train machine learning models can show up in the model's behavior. 

Amazon Comprehend is a service that, amongst many thing, provides sentiment analysis to given text documents. The following project tests what would happen if we took an identical block of text and ran it through Comprehend multiple times, only changing the perceived race of the names in the text and the gender pronouns. We'll generate a data set, run it through Comprehend to get sentiment results, and then implement a two-sample heteroscedastic t-Test for equal means to determine the statistical significance that sentiment score (confidence in the calculated sentiment type) means are equal across a population. 

## Data

### Base Text
The base test used for this experiment is a passage from the "Hitchhiker's Guide to the Galaxy" and was arbitrarily chosen. The comments were removed manually to make exporting to csv easier. The passage is as follows:

  - "The only person for whom the house was in any way special was Arthur Dent and that was only because it happened to be the one he lived in. He had lived in it for about three years ever since he had moved out of London because it made him nervous and irritable. He was about thirty as well tall dark-haired and never quite at ease with himself. The thing that used to worry him most was the fact that people always used to ask him what he was looking so worried about. He worked in local radio which he always used to tell his friends was a lot more interesting than they probably thought. It was too almost of his friends worked in advertising. On Wednesday night it had rained very heavily the lane was wet and muddy but the Thursday morning sun was bright and clear as it shone on Arthur Dent's house for what was to be the last time. It hadn't properly registered yet with Arthur that the council wanted to knock it down and build a bypass instead."

### Name Data
The name data is pulled from the book "Freakonomics" by Stephen J. Dubner and Steven Levitt. In the book, the authors describe a California study which aimed to highlight the effect of names on socioeconomic status, etc. This researcher parsed through California baby name data and produced a list of the top 20 black male, white male, black female, and white female names in California. I used these names demonstrate the differences between race and gender. For example, the t-Test will compare all black male names to all black female names to indicate gender-based bias or all white male names to all black male names to indicate race-based bias. You can read more about the study and underlying data here: https://myweb.ntut.edu.tw/~kmliu/freakonomics/6%20roshanda.pdf. 

### Comprehend Result Data
The Comprehend result data takes one line in a file which includes a single passage of text and then returns the dominant sentiment type as well as the confidence in each of the four pre-defined sentiment types. Example: 

    File: text_only.csv
    Line: 0
    Sentiment: POSITIVE
    SentimentScore__Mixed: 0.000776149
    SentimentScore__Negative: 0.185558125
    SentimentScore__Neutral: 0.343352616 
    SentimentScore__Positive: 0.470313102

## Testing Methodology 

### High level process
The `TextPassage` class includes the base text as well as lists of all 80 names. There are static methods to substitute "Arthur" with any of the provided names as well as a method to convert gender pronouns from the default male to female. Nonbinary pronouns were not supported this time. The `GenerateSampleDataReport` class then creates the 80 sample text passages using the name/gender data and saves to a csv file. After that, the data is (for now) manually uploaded to an S3 bucket and a custom Comprehend job is created to generate sentiment data for each row. Each row represents a single name. The data is returned back in the format specified above. The `SentimentCSVParser` then parses the results and generates two Lists of double arrays (size 2), one based on race and one on gender:

    Race: The first array in the returned list represents the positive sentiment scores (confidence) for each "white sounding" name in the data set,   
    male and female. The second array represents the positive sentiment scores (confidence) for each "black sounding" name in the data set, male and
    female.
    
    Gender: The first array in the returned list represents the positive sentiment scores (confidence) for each "female sounding" name in the data set,
    black and white. The second array represents the positive sentiment scores (confidence) for each "male sounding" name in the data set, black and
    white.

Alpha is set in the GetResults class and a t-Test is run in the main method by calling the t-Test implementation in `RunTTest`.

### t-Test implementation
Performs a two-sided t-test evaluating the null hypothesis that sample1 and sample2 are drawn from populations with the same mean, with significance level alpha.  This test does not assume that the subpopulation variances are equal. Returns true iff the null hypothesis that the means are equal can be rejected with confidence 1 - alpha. Degrees of freedom are approximated using the Welch-Satterthwaite approximation. The Welch-Satterthwaite approximation is used to to estimate degrees of freedom and it assumes that the underlying distributions are independent and normal. It does not assume that the variances in each population are the same. 


Examples:

To test the (2-sided) hypothesis mean 1 = mean 2  at the 95% level,  use tTest(sample1, sample2, 0.05). To test the (one-sided) hypothesis  mean 1 < mean 2 , at the 99% level, first verify that the measured  mean of sample 1 is less than the mean of sample 2 and then use tTest(sample1, sample2, 0.02)

## Results
Using the data above we can obtain the following results: 

    At an alpha level of 0.1, can we reject the hypothesis that black names and white names have the same mean positive sentiment score: true
    At an alpha level of 0.1, can we reject the hypothesis that male names and female names have the same mean positive sentiment score: false

## Conclusions and Next Steps
There appears to be bias in Comprehend's sentiment analysis models, especially with respect to race. However, this study was a quick look, not an exhaustive analysis. Going forward, I would suggest a number of things to get a better idea of any actual bias in Comprehend. This list isn't exhaustive, but includes: 

1. Use longer sample text and run the experiment with a wider number of base text blurbs. 
2. Increase the number of names dramatically. We only have 80 records in this test. 
3. Calculate the confidence interval 
4. Do more checks on input data such as looking for whether the data is normally distributed (an important assumption in these tests)
5. TBD


This is a work in progress. 




