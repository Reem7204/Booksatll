from  collections import Counter
import re
WORD=re.compile((r'\w+'))
import math
from src.dbconnection import *

def text_to_vector(text):
    words = WORD.findall(text)
    return Counter(words)

def get_cosine(vec1, vec2):
    intersection = set(vec1.keys()) & set(vec2.keys())
    numerator = sum([vec1[x] * vec2[x] for x in intersection])
    sum1 = sum([vec1[x] ** 2 for x in vec1.keys()])
    sum2 = sum([vec2[x] ** 2 for x in vec2.keys()])
    denominator = math.sqrt(sum1) * math.sqrt(sum2)

    if not denominator:
        return 0.0
    else:
        return float(numerator) / denominator
def recom(txt,bid,uid):
    result=['0']
    qry="SELECT `book`.`b_id`,book.description FROM `book`  WHERE `book`.`b_id` NOT IN(SELECT `book`.`b_id` FROM `book` JOIN `userpd` ON `userpd`.`b_id`=`book`.`b_id` LEFT JOIN `userpm` ON `userpm`.`pm_id`=`userpd`.`pm_id` WHERE `userpm`.`cust_id`=%s)"
    res=selectall2(qry,uid)
    vec1=text_to_vector(txt)
    for i in res:
        vec2=text_to_vector(i[1])
        sim=get_cosine(vec1,vec2)
        if sim>0.5:
            result.append(str(i[0]))


    return ",".join(result)


