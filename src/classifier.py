import pandas as pd
import  nltk
import string
# nltk.download('stopwords')
# nltk.download('punkt')
from flask import *
import pymysql
app=Flask(__name__)
app.secret_key="a"
con = pymysql.connect(host='localhost', user='root', passwd='', port=3306, db='books')
cmd = con.cursor()

class CLASSIFIER:
    def __init__(self):
        self.dataset_path=r"C:\Users\User\Downloads\New folder (11)\productreview\src\static\dataset\fake_reviews_dataset.csv"
        self.x=[]  #feature
        self.y=[]  #label
        self.fake_words = []   #fake review words on dataset
        self.orig_words = []   #label review on dataset
    def get_data(self):
        data = pd.read_csv(self.dataset_path) #pd to read csv file
        self.x = data.values[:, 3]  # review
        self.y = data.values[:, 2]  # labels
    def categorize_words(self):
        for i in range(len(self.x)):
            review = self.x[i]
            processed_words = self.preprocess_data(review)
            for word in processed_words:
                if self.y[i] == "CG":
                    self.fake_words.append(word)
                elif self.y[i] == "OR":
                    self.orig_words.append(word)
            if i==1000:
                break
        s1 = set(self.fake_words)
        self.fake_words=list(s1)
        s2=set(self.orig_words)
        self.orig_words=list(s2)
        return self.fake_words, self.orig_words


    def preprocess_data(self, text):
        stopwords = nltk.corpus.stopwords.words('english')
        punctuation = string.punctuation
        remove_punct = "".join([word.lower() for word in text if word not in punctuation])          ###     removing punctuation
        tokenized = nltk.tokenize.word_tokenize(remove_punct)                                       ###     splitting to words
        remove_stopwords =[word for word in tokenized if word not in stopwords]
        return remove_stopwords




    def classify(self, review):
        fake_counter = 0
        orig_counter = 0

        for word in review:
            fake_counter += self.fake_words.count(word)
            orig_counter += self.orig_words.count(word)

        if fake_counter==0:
            fake_counter=1
            if orig_counter==0:
                orig_counter=1

        print("Counters  ")
        print("Fake counters  ", fake_counter)
        print("Orig counters  ", orig_counter)

        if orig_counter > fake_counter:
            acc = round(orig_counter / (orig_counter + fake_counter)*100)       #   accuracy
            print("Review is not fake, with {}% certainty".format(acc))
            print( "Original Review")
            return "orginal"

        elif orig_counter == fake_counter:
            acc = round(orig_counter / (orig_counter + fake_counter) * 100)
            print("Review could be fake, with {}% certainty".format(acc))
            print( "Original Review")
            return "orginal"
        else:
            acc = round(fake_counter / (orig_counter + fake_counter) * 100)
            print("Review is fake, with {}% certainty".format(acc))
            print( "Fake Review")
            return "fake"

    def run(self, review):
        ###         reading from csv file
        self.get_data()
        self.fake_words, self.orig_words =self.categorize_words()
        processed_input = self.preprocess_data(review)
        status = self.classify(processed_input)
        print("Final Prediction :==========================================  "+status)
        # pid=0
        # cmd.execute("INSERT INTO fake values(null, '" + str(session['pid']) + "','" + review + "',curdate(),'" + status + "')")
        # con.commit()
        return status
##########    DATASET VALUES FOR LABEL
###     CG - FAKE
###     OR - ORIGINAL
###########
# review_to_check="I think I made a mistake. The shipping was quick and it arrived within 2 weeks.  I will be buying the same.Great product and is the best product I have purchased. The only thing I would"
# cl=CLASSIFIER()
# cl.run(review_to_check)