import os
from flask import *
from werkzeug.utils import secure_filename
import datetime
from datetime import datetime,timedelta
from src.dbconnection import *
import smtplib
from src.rec import recom
from email.mime.text import MIMEText
from flask_mail import Mail


app = Flask(__name__)
mail=Mail(app)
app.config['MAIL_SERVER']='smtp.gmail.com'
app.config['MAIL_PORT'] = 587
app.config['MAIL_USERNAME'] = 'amanbookstall3@gmail.com'
app.config['MAIL_PASSWORD'] = 'rekluwmicbnbmjqt'
app.config['MAIL_USE_TLS'] = False
app.config['MAIL_USE_SSL'] = True


@app.route('/logincode',methods=['post'])
def logincode():
    uname = request.form['uname']
    pswd = request.form['pass']
    qry = "select * from login where username = %s and password = %s "
    val = (uname,pswd)
    s = selectone(qry,val)
    if s is None:
        return jsonify({'task':'invalid'})
    else:
        type=s[3]
        id=s[0]
        return jsonify({'task': 'success','id':id,'type':type})



@app.route('/registrationcode',methods=['post'])
def registrationcode():
    name = request.form['name']
    place = request.form['place']
    post = request.form['post']
    pin = request.form['pin']
    emailid = request.form['emailid']
    phoneno = request.form['phoneno']

    password = request.form['pass']
    q = "select * from login where username=%s"
    v = (emailid)
    r = selectone(q,v)
    print(r)
    if r is None:
        qry1 = "insert into login values (NULL,%s,%s,'Publisher')"
        val1 = (emailid, password)
        lid = iud(qry1, val1)

        qry = "insert into publisher values(NULL,%s,%s,%s,%s,%s,%s,%s)"
        val = (name,place,post,pin,emailid,phoneno,lid)
        iud(qry,val)

        return jsonify({'task':'success'})
    else:
        return jsonify({'task': 'exist'})


@app.route('/registrationcode2',methods=['post'])
def registrationcode2():
    fname = request.form['fname']
    lname = request.form['lname']
    place = request.form['place']
    post = request.form['post']
    pin = request.form['pin']
    emailid = request.form['emailid']
    phoneno = request.form['phoneno']

    password = request.form['pass']
    q = "select * from login where username=%s"
    v = (emailid)
    r = selectone(q, v)
    if r is None:
        qry1 = "insert into login values (NULL,%s,%s,'Customer')"
        val1 = (emailid, password)
        lid = iud(qry1, val1)

        qry = "insert into customer values(NULL,%s,%s,%s,%s,%s,%s,%s,%s)"
        val = (fname,lname,place,post,pin,phoneno,emailid,lid)
        iud(qry,val)

        return jsonify({'task':'success'})
    else:
        return jsonify({'task': 'exist'})


@app.route('/pcode',methods=['post'])
def pcode():
    print(request.form)
    id = request.form['lid']
    print(id)
    fname = request.form['fname']
    lname = request.form['lname']
    place = request.form['place']
    post = request.form['post']
    pin = request.form['pin']

    phoneno = request.form['phoneno']

    password = request.form['pass']

    qry1 = "update login set password=%s where l_id=%s"
    val1 = (password,id)
    iud(qry1,val1)

    qry = "Update customer set `fname`=%s, `lname`=%s, `place`=%s, `post`=%s, `pin`=%s, `phoneno`=%s where `l_id`=%s"
    val = (fname,lname,place,post,pin,phoneno,id)
    iud(qry,val)

    return jsonify({'task':'success'})



@app.route('/pcode2',methods=['post'])
def pcode2():
    print(request.form)
    id = request.form['lid']
    print(id)
    fname = request.form['fname']

    place = request.form['place']
    post = request.form['post']
    pin = request.form['pin']

    phoneno = request.form['phoneno']

    password = request.form['pass']

    qry1 = "update login set password=%s where l_id=%s"
    val1 = (password,id)
    iud(qry1,val1)

    qry = "Update publisher set `name`=%s, `place`=%s, `post`=%s, `pin`=%s, `phoneno`=%s where `lid`=%s"
    val = (fname,place,post,pin,phoneno,id)
    iud(qry,val)

    return jsonify({'task':'success'})



@app.route('/addbook',methods=['post'])
def addbook():
    title = request.form['title']
    author = request.form['author']
    isbn = request.form['isbn']
    publishdate = request.form['publishdate']
    genre = request.form['genre']
    pageno = request.form['pageno']
    price = request.form['price']
    description = request.form['description']
    pic1 = request.files['file']
    pic1name=secure_filename(pic1.filename)
    pic1.save(os.path.join('static/bookpic',pic1name))

    pic2 = request.files['file2']
    pic2name = secure_filename(pic2.filename)
    pic2.save(os.path.join('static/bookpic', pic2name))
    lid = request.form['lid']
    q = "select * from p_book where p_id=%s and isbn=%s "
    v = (lid,isbn)
    r = selectone(q,v)
    if r is None:
        qry = "insert into p_book values(NULL,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s)"
        val = (title,author,publishdate,genre,pageno,description,price,pic1name,pic2name,lid,isbn)
        iud(qry,val)

        return jsonify({'task':'success'})
    else:
        return jsonify({'task': 'exist'})


@app.route('/updatebook',methods=['post'])
def updatebook():
    print(request.form)
    id = request.form['id']
    title = request.form['title']
    author = request.form['author']
    isbn = request.form['isbn']
    publishdate = request.form['publishdate']
    genre = request.form['genre']
    pageno = request.form['pageno']
    price = request.form['price']
    description = request.form['description']

    lid = request.form['lid']

    qry = "Update p_book set title=%s,author=%s,publishdate=%s,genre=%s,pageno=%s,description=%s,price=%s,isbn=%s where id=%s "
    val = (title,author,publishdate,genre,pageno,description,price,isbn,id)
    iud(qry,val)

    return jsonify({'task':'success'})


@app.route('/updateimg',methods=['post'])
def updateimg():
    pic1 = request.files['file']
    id=request.form['lid']
    pic1name = secure_filename(pic1.filename)
    pic1.save(os.path.join('static/bookpic', pic1name))

    # pic2 = request.files['file2']
    # pic2name = secure_filename(pic2.filename)
    # pic2.save(os.path.join('static/bookpic', pic2name))
    # lid = request.form['lid']
    qry = "update p_book set pic1=%s where id=%s"
    val = (pic1name,id)
    iud(qry,val)
    return jsonify({'task':'success','fname':pic1name})


@app.route('/updateimg2',methods=['post'])
def updateimg2():
    pic1 = request.files['file2']
    print("****")
    id=request.form['lid']
    pic1name = secure_filename(pic1.filename)
    pic1.save(os.path.join('static/bookpic', pic1name))

    # pic2 = request.files['file2']
    # pic2name = secure_filename(pic2.filename)
    # pic2.save(os.path.join('static/bookpic', pic2name))
    # lid = request.form['lid']
    qry = "update p_book set pic2=%s where id=%s"
    val = (pic1name,id)
    iud(qry,val)
    return jsonify({'task':'success','fname':pic1name})


@app.route('/deletebooks',methods=['post'])
def deletebooks():
    print(request.form)
    pub_id = request.form['id']
    print(pub_id)
    qry = "delete from p_book WHERE `id`=%s"
    iud(qry,pub_id)

    return jsonify({'task':'success'})


@app.route('/cancelbookorder',methods=['post'])
def cancelbookorder():

    pub_id = request.form['id']
    print(pub_id)
    qry = "update userpm set status='Cancel' where pm_id=%s"
    iud(qry,pub_id)

    return jsonify({'task':'success'})


@app.route('/viewbooks',methods=['post'])
def viewbooks():
    print(request.form)
    pub_id = request.form['pid']
    qry = "select *,DATE_FORMAT(p_book.publishdate,'%d-%m-%Y') AS dd from p_book WHERE `p_id`='"+str(pub_id)+"'"
    res=androidselectallnew(qry)
    print(res)
    return jsonify(res)

@app.route('/viewrequest',methods=['post'])
def viewrequest():
    print(request.form)
    pub_id = request.form['pid']
    qry = "select *,DATE_FORMAT(paddcart.date,'%d-%m-%Y') AS dd from `p_book` join `paddcart` On `p_book`.`id`=`paddcart`.`bookid` WHERE`p_book`.`p_id`='"+str(pub_id)+"' and `paddcart`.`status`='Requested'"
    res=androidselectallnew(qry)
    print(res)
    return jsonify(res)


@app.route('/a_request',methods=['post'])
def a_request():
    print(request.form)
    pub_id = request.form['pid']
    qry = "SELECT *,DATE_FORMAT(paddcart.date,'%d-%m-%Y') AS d1,DATE_FORMAT(paddcart.d_date,'%d-%m-%Y') AS d2 FROM `p_book` JOIN `paddcart` ON `p_book`.`id`=`paddcart`.`bookid` WHERE`p_book`.`p_id`='"+str(pub_id)+"' AND `paddcart`.`status`='Accepted'"
    res=androidselectallnew(qry)
    print(res)
    return jsonify(res)



@app.route('/forgotpassword1',methods=['post'])
def forgotpassword1():
    print(request.form)
    try:
        print(request.form)
        email = request.form['textfield']
        print(email)
        qry = "SELECT `login`.`password` FROM `publisher`  JOIN `login` ON `login`.`l_id` = `publisher`.`lid` WHERE emailid=%s"
        s = selectone(qry, email)
        print(s, "=============")
        if s is None:
            return jsonify({'task': 'invalid email'})
        else:
            try:
                gmail = smtplib.SMTP('smtp.gmail.com', 587)
                gmail.ehlo()
                gmail.starttls()
                gmail.login('amanbookstall3@gmail.com', 'rekluwmicbnbmjqt')
            except Exception as e:
                print("Couldn't setup email!!" + str(e))
            msg = MIMEText("Your password is : " + str(s[0]))
            print(msg)
            msg['Subject'] = 'Your Password'
            msg['To'] = email
            msg['From'] = 'amanbookstall3@gmail.com'
            try:
                gmail.send_message(msg)
            except Exception as e:
                print("COULDN'T SEND EMAIL", str(e))
            return '''<script>alert("SEND"); window.location="/"</script>'''
    except:
        return '''<script>alert("PLEASE ENTER VALID DETAILS"); window.location="/"</script>'''


@app.route('/genreselect',methods=['post'])
def genreselect():
    qry = "select* from section"
    res = androidselectallnew(qry)
    return jsonify(res)

@app.route('/custviewbook',methods=['post'])
def custviewbook():
    print(request.form)
    title=request.form['title']
    sid=request.form['sid']
    print(sid)
    if title =="":
        print("nnnn")
        if sid=='0':
            qry = "SELECT *,ROUND(AVG(`crawlresult`.`rating`),1) AS r,DATE_FORMAT(book.p_year,'%d-%m-%Y') AS dd FROM book JOIN section JOIN `crawlresult` ON section.s_id=book.s_id AND `book`.`b_id`=`crawlresult`.`product_id` GROUP BY `product_id`"
            res = androidselectallnew(qry)
            print(res)
            return jsonify(res)
        else:

            qry = "SELECT *,ROUND(AVG(`crawlresult`.`rating`),1) AS r,DATE_FORMAT(book.p_year,'%d-%m-%Y') AS dd FROM book JOIN section JOIN `crawlresult` ON section.s_id=book.s_id AND `book`.`b_id`=`crawlresult`.`product_id` where book.s_id=%s GROUP BY `product_id`"
            res =androidselectall(qry,sid)
            print("hf",res)
            return jsonify(res)
    else:
        print("UUUU")
        qry="SELECT *,ROUND(AVG(`crawlresult`.`rating`),1) AS r,DATE_FORMAT(book.p_year,'%d-%m-%Y') AS dd FROM book JOIN section JOIN `crawlresult` ON section.s_id=book.s_id AND `book`.`b_id`=`crawlresult`.`product_id` WHERE book.title like '%"+str(title)+"%' GROUP BY `product_id`"
        res = androidselectallnew(qry)
        return jsonify(res)


@app.route('/pubviewbook',methods=['post'])
def pubviewbook():
    print(request.form)
    title=request.form['title']
    pid=request.form['pid']
    q = "select *, DATE_FORMAT(p_book.publishdate, '%d-%m-%Y') AS dd from p_book where `p_id` = '"+str(pid)+"' and title like '%"+str(title)+"%'"
    res = androidselectallnew(q)
    return jsonify(res)




@app.route('/View_Books_rec',methods=['post'])
def View_Books_rec():

    id=request.form['lid']
    qry=" SELECT `book`.`b_id`,`description` FROM `book` JOIN `userpd` ON `userpd`.`b_id`=`book`.`b_id` LEFT JOIN `userpm` ON `userpm`.`pm_id`=`userpd`.`pm_id` WHERE `userpm`.`cust_id`=%s"
    res=selectone(qry,id)
    print(res)
    if res is None:
        return jsonify([])
    else:
        txt=res[1]
        bid=res[0]
        res=recom(txt,bid,id)
        qry="SELECT * FROM `book` join section on book.s_id=section.s_id WHERE `b_id` IN ("+res+")"
        res=androidselectallnew(qry)
        print(res)
        return jsonify(res)



@app.route('/acceptrequest',methods=['post'])
def acceptrequest():

    print(request.form)
    id = request.form['id']
    d = request.form['d']
    qry = "update paddcart set `status`='Accepted',`d_date`=%s where id=%s"
    val = (d,id)
    iud(qry,val)
    return jsonify({'task': 'success'})



@app.route('/rejectrequest',methods=['post'])
def rejectrequest():
    print(request.form)
    id = request.form['id']
    qry = "update paddcart set `status`='Rejected' where id=%s"
    val = (id)
    iud(qry,val)
    return jsonify({'task': 'success'})


@app.route('/cancelbooks',methods=['post'])
def cancelbooks():
    print(request.form)
    id = request.form['idd']
    qry = "update paddcart set `status`='Cancelled' where id=%s"
    val = (id)
    iud(qry,val)
    return jsonify({'task': 'success'})



@app.route('/service',methods=['post'])
def service():
    print(request.form)
    id = request.form['pid']
    qry = "SELECT `p_book`.`title`,`paddcart`.`qty`,`paddcart`.`date` FROM `paddcart` JOIN `p_book` ON `p_book`.`id`=`paddcart`.`bookid` WHERE `paddcart`.`status`='Requested' AND `p_book`.`p_id`=%s"
    val = (id)
    res=selectone(qry,val)
    if res is None:
        return jsonify({'task1': 'na'})
    else:
        return jsonify({'task1': 'yes'})


@app.route('/cancelservice',methods=['post'])
def cancelservice():
    print(request.form)
    id = request.form['pid']
    qry = "SELECT `p_book`.`title`,`paddcart`.`qty`,`paddcart`.`date` FROM `paddcart` JOIN `p_book` ON `p_book`.`id`=`paddcart`.`bookid` WHERE `paddcart`.`status`='Cancel' AND `p_book`.`p_id`=%s"
    val = (id)
    res=selectone(qry,val)
    if res is None:
        return jsonify({'task1': 'na'})
    else:
        return jsonify({'task1': 'yes'})

@app.route('/addreview',methods=['post'])
def addreview():
    review = request.form['review']
    id2 = request.form['bid']
    print(id2)
    id = request.form['pid']
    print(id)

    d = datetime.now().strftime("%Y-%m-%d")
    qry = "insert into review values(NULL,%s,%s,%s,%s)"
    val = (id,d,review,id2)
    iud(qry,val)
    return jsonify({'task': 'success'})



@app.route('/addsuggestion',methods=['post'])
def addsuggestion():
    s = request.form['s']
    id = request.form['pid']
    print(id)
    d = datetime.now().strftime("%Y-%m-%d")
    qry = "insert into suggestion values(NULL,%s,%s,%s)"
    val = (d,s,id)
    iud(qry,val)
    return jsonify({'task': 'success'})


@app.route('/addcomplaint',methods=['post'])
def addcomplaint():
    s = request.form['s']
    id = request.form['pid']
    print(id)
    d = datetime.now().strftime("%Y-%m-%d")
    qry = "insert into complaint values(NULL,%s,%s,'-',%s)"
    val = (id,s,d)
    iud(qry,val)
    return jsonify({'task': 'success'})



@app.route('/addtocart',methods=['post'])
def addtocart():
    q = request.form['q']
    id = request.form['bid']
    cid = request.form['lid']
    print(id)
    d = datetime.now().strftime("%Y-%m-%d")
    qry = "insert into userpm values(NULL,%s,%s,'Requested')"
    val = (cid,d)
    r = iud(qry,val)
    qry2 = "insert into userpd values(NULL,%s,%s,%s)"
    val2 = (r,id,q)
    iud(qry2,val2)

    return jsonify({'task': 'success'})



@app.route('/viewreview',methods=['post'])
def viewreview():
    id = request.form['bid']
    print(id)
    qry = "SELECT *,DATE_FORMAT(review.date,'%d-%m-%Y') as dd FROM review LEFT JOIN customer ON review.cust_id=customer.l_id where review.bid='"+str(id)+"' ORDER BY r_id DESC "
    res = androidselectallnew(qry)
    print(res)
    return jsonify(res)


@app.route('/viewmycart',methods=['post'])
def viewmycart():
    id = request.form['pid']
    print(id)
    qry = "SELECT *,userpm.status AS s,DATE_FORMAT(userpm.date,'%d-%m-%Y') AS dd FROM `userpm` JOIN `userpd` JOIN book ON `userpm`.`pm_id`=`userpd`.`pm_id` AND userpd.b_id=book.b_id WHERE `userpm`.`cust_id`='"+str(id)+"' AND `userpm`.`status` IN ('Requested','Accepted') ORDER BY `date` DESC"
    res = androidselectallnew(qry)
    print(res)
    return jsonify(res)


@app.route('/history2',methods=['post'])
def history2():
    id = request.form['pid']
    print(id)
    qry = "SELECT *,userpm.status AS s,DATE_FORMAT(userpm.date,'%d-%m-%Y') AS dd FROM `userpm` JOIN `userpd` JOIN book ON `userpm`.`pm_id`=`userpd`.`pm_id` AND userpd.b_id=book.b_id WHERE `userpm`.`cust_id`='"+str(id)+"' ORDER BY `date` DESC"
    res = androidselectallnew(qry)
    print(res)
    return jsonify(res)



@app.route('/updatep1',methods=['post'])
def updatep1():
    lid = request.form['pid']
    print(lid)
    qry = "SELECT * FROM customer JOIN `login` ON `login`.`l_id`=`customer`.`l_id` WHERE `customer`.`l_id`=%s"
    res = androidselectall(qry,lid)
    print(res)
    return jsonify(res)


@app.route('/updatep2',methods=['post'])
def updatep2():
    lid = request.form['pid']
    print(lid)
    qry = "SELECT * FROM `publisher` JOIN `login` ON `publisher`.`lid`=`login`.`l_id` WHERE `publisher`.`lid`=%s"
    res = androidselectall(qry,lid)
    print(res)
    return jsonify(res)


@app.route('/complaint2',methods=['post'])
def complaint2():
    id = request.form['pid']
    print(id)
    qry = "select *,DATE_FORMAT(`date`,'%d-%m-%Y') AS dd from complaint where cust_id='"+str(id)+"'"
    res = androidselectallnew(qry)
    print(res)
    return jsonify(res)


@app.route('/history',methods=['post'])
def history():
    id = request.form['pid']
    print(id)
    qry = "SELECT *,DATE_FORMAT(p_book.publishdate,'%d-%m-%Y') AS pd,DATE_FORMAT(paddcart.date,'%d-%m-%Y') AS d1,DATE_FORMAT(paddcart.d_date,'%d-%m-%Y') AS d2 FROM `paddcart` JOIN `p_book` ON `p_book`.`id`=`paddcart`.`bookid` WHERE p_id='"+str(id)+"'"
    res = androidselectallnew(qry)
    print("+++++++++++++",res)
    return jsonify(res)


@app.route('/cancelrequest',methods=['post'])
def cancelrequest():
    id = request.form['pid']
    print(id)
    qry = "SELECT *,`paddcart`.`id` AS idd,DATE_FORMAT(p_book.publishdate,'%d-%m-%Y') AS pd,DATE_FORMAT(paddcart.date,'%d-%m-%Y') AS d1,DATE_FORMAT(paddcart.d_date,'%d-%m-%Y') AS d2 FROM `paddcart` JOIN `p_book` ON `p_book`.`id`=`paddcart`.`bookid` WHERE p_id='"+str(id)+"' AND `status`='Cancel'"
    res = androidselectallnew(qry)
    print("+++++++++++++",res)
    return jsonify(res)

@app.route('/countcancel',methods=['post'])
def countcancel():
    id = request.form['lid']
    print(id)
    qry = "SELECT COUNT(`paddcart`.`id`) AS c FROM `paddcart` JOIN `p_book` ON `p_book`.`id`=`paddcart`.`bookid` WHERE p_id=%s AND `status`='Cancel'"
    res = selectone(qry,id)
    print("+++++++++++++",res)
    return jsonify({'task': res[0]})



app.run(host="0.0.0.0",port=5000)