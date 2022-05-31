from flask import *
from src.dbconnection import *
import os
import datetime
from datetime import datetime,timedelta
from werkzeug.utils import secure_filename
app = Flask(__name__)
app.secret_key="sdsd"


@app.route('/')
def first():
    return render_template("index.html")


@app.route('/login')
def login():
    return render_template("login1.html")



@app.route('/logincode',methods=['post'])
def logincode():
    uname = request.form['username']
    pswd = request.form['password']
    qry = "select * from login where username = %s and password = %s"
    val = (uname,pswd)
    s = selectone(qry,val)
    if s is None:
        return '''<script>alert('Invalid username and password');window.location='/login';</script>'''
    elif s[3]=="Admin":
        return redirect('/adminhome')
    elif s[3]=="Employee":
        return redirect('/employeehome')
    else:
        return '''<script>alert('Invalid username and password');window.location='/login';</script>'''



@app.route('/blankpage')
def blankpage():
    return render_template("blankpage.html")

@app.route('/adminhome')
def adminhome():
    return render_template("adminhome.html")

@app.route('/employeehome')
def employeehome():
    return render_template("employeehome.html")


@app.route('/addsection')
def addsection():
    return render_template("addsection.html")

@app.route('/addemployee')
def addemployee():
    return render_template("addemployee.html")


@app.route('/dpublisherpurchase')
def dpublisherpurchase():
    qry = "select * from section"
    res = selectall(qry)
    return render_template("dpublisherpurchase.html",val1=res)


@app.route('/dpuinsert')
def dpuinsert():
    b = request.args.get('Submit')
    if b == "Add book":
        q = "SELECT MAX(dp_id),`status` FROM `dup_master` WHERE `dp_id` IN(SELECT MAX(dp_id) FROM `dup_master`)"

        res = selectone2(q)
        print(res)
        if res[1]!='pending':
            name = request.args.get('name')
            phno = request.args.get('phno')
            emailid = request.args.get('emailid')
            d = datetime.now()
            title = request.args.get('title')
            quantity = request.args.get('quantity')

            qry2 = "INSERT INTO `dup_master` (`name`,`phoneno`,`emailid`,`date`,`status`) VALUES (%s,%s,%s,%s,'pending')"
            val2 = (name,phno,emailid,d)
            id = iud(qry2, val2)


            qry = "SELECT * FROM book where title=%s"
            val = (title)
            res = selectone(qry, val)

            qry3 = "INSERT INTO `dup_detail` (`id`,`dp_id`,`bid`,`quantity`) VALUES (NULL,%s,%s,%s)"
            val3 = (id, res[0], quantity)
            session['d1_id'] = id
            iud(qry3, val3)
            return '''<script>alert('Item entered');window.location='/duserpurchase2';</script>'''


        elif res[1]=='pending':


            title = request.args.get('title')

            quantity = request.args.get('quantity')

            qry = "SELECT * FROM book where title=%s"
            val = (title)
            res = selectone(qry,val)
            print(res)

            q = "SELECT * FROM `dup_master` WHERE `status`='pending' AND `dp_id` IN(SELECT MAX(dp_id) FROM `dup_master`)"

            r = selectone2(q)
            print(r)

            qry3 = "INSERT INTO `dup_detail` (`id`,`dp_id`,`bid`,`quantity`) VALUES (NULL,%s,%s,%s)"
            val3 = (r[0],res[0],quantity)

            print(r[0])
            iud(qry3,val3)

            return '''<script>alert('Item entered');window.location='/duserpurchase2';</script>'''
        else:
            return '''<script>alert('Error');window.location='/duserpurchase';</script>'''

    else:
        qry6 = "select MAX(dp_id) from dup_master"
        res6 = selectone2(qry6)
        qry5 = "Update dup_master set `status`='finished' where dp_id=%s"
        val5 = (res6[0])
        iud(qry5,val5)
        return '''<script>alert('Added successfully');window.location='/viewbill';</script>'''



@app.route('/dpuinsert1')
def dpuinsert1():
    b = request.args.get('Submit')
    if b == "Add book":
        q = "SELECT MAX(du_id),`status` FROM `dpp_master` WHERE `du_id` IN(SELECT MAX(du_id) FROM `dpp_master`)"

        res = selectone2(q)
        print(res)
        if res[1]!='pending':
            name = request.args.get('name')
            address = request.args.get('address')
            phno = request.args.get('phoneno')
            emailid = request.args.get('emailid')
            d = datetime.now()
            title = request.args.get('title')
            quantity = request.args.get('quantity')
            isbn = request.args.get('isbn')
            genre = request.args.get('genre')
            author = request.args.get('author')
            price = request.args.get('price')

            qry2 = "INSERT INTO `dpp_master` (`name`,`address`,`phoneno`,`emailid`,`date`,`status`) VALUES (%s,%s,%s,%s,%s,'pending')"
            val2 = (name,address,phno,emailid,d)
            id = iud(qry2, val2)

            qry3 = "INSERT INTO `dpp_detail` (`id`,`du_id`,`isbn`,`s_id`,`title`,`author`,`price`,`quantity`) VALUES (NULL,%s,%s,%s,%s,%s,%s,%s)"
            val3 = (id,isbn,genre,title,author,price,quantity)
            session['d2_id'] = id
            iud(qry3, val3)
            return '''<script>alert('Item entered');window.location='/dpublisherpurchase2';</script>'''


        elif res[1]=='pending':

            title = request.args.get('title')
            quantity = request.args.get('quantity')
            isbn = request.args.get('isbn')
            genre = request.args.get('genre')
            author = request.args.get('author')
            price = request.args.get('price')

            q = "SELECT * FROM `dpp_master` WHERE `status`='pending' AND `du_id` IN(SELECT MAX(du_id) FROM `dpp_master`)"

            r = selectone2(q)
            print(r)

            qry3 = "INSERT INTO `dpp_detail` (`id`,`du_id`,`isbn`,`s_id`,`title`,`author`,`price`,`quantity`) VALUES (NULL,%s,%s,%s,%s,%s,%s,%s)"
            val3 = (r[0],isbn,genre,title,author,price,quantity)

            print(r[0])
            iud(qry3,val3)

            return '''<script>alert('Item entered');window.location='/dpublisherpurchase2';</script>'''
        else:
            return '''<script>alert('Error');window.location='/dpublisherpurchase';</script>'''

    else:
        qry6 = "select MAX(du_id) from dpp_master"
        res6 = selectone2(qry6)
        qry5 = "Update dpp_master set `status`='finished' where du_id=%s"
        val5 = (res6[0])
        iud(qry5,val5)
        return '''<script>alert('Saved successfully');window.location='/dpublisherpurchase';</script>'''




@app.route('/bookdetails',methods=['get','post'])
def bookdetails():
    title = request.form['brand']
    print(title)
    qry="SELECT author,price,isbn FROM book WHERE title=%s"
    s=selectone(qry,title)
    if s is None:
        return "no value"
    else:
        print(s)
    # lis=[0,'select']
    # for r in s:
    #     lis.append(r[0])
    #     lis.append(r[2])
    # print(lis)
        resp = make_response(jsonify(s))
        resp.status_code = 200
        resp.headers['Access-Control-Allow-Origin'] = '*'
        return resp






@app.route('/duserpurchase', methods=["POST", "GET"])
def duserpurchase():

    if request.method == "GET":
        qry = "select * from book"
        languages=selectall(qry)

        return render_template("s.html",languages=languages)



@app.route('/duserpurchase2', methods=["POST", "GET"])
def duserpurchase2():

    if request.method == "GET":
        qry = "select * from book"
        languages=selectall(qry)

        qry0 = "SELECT * FROM book JOIN `dup_master` JOIN `dup_detail` ON `book`.`b_id`=`dup_detail`.`bid` AND `dup_master`.`dp_id`=`dup_detail`.`dp_id` WHERE `dup_master`.dp_id=%s"
        val0 = (session['d1_id'])
        res0 = selectall2(qry0, val0)

        qry1="SELECT SUM(`dup_detail`.`quantity`*`book`.`price`) FROM book JOIN `dup_master` JOIN `dup_detail` ON `book`.`b_id`=`dup_detail`.`bid` AND `dup_master`.`dp_id`=`dup_detail`.`dp_id` WHERE `dup_master`.dp_id=%s"
        res1=selectone(qry1,val0)

        return render_template("duserpurchase.html",languages=languages,val=res0,p=res1[0])



@app.route('/dpublisherpurchase2', methods=["POST", "GET"])
def dpublisherpurchase2():

    if request.method == "GET":
        qry2 = "select * from section"
        res2 = selectall(qry2)

        qry = "select * from book"
        languages=selectall(qry)

        qry0 = "SELECT * FROM `dpp_master` JOIN `dpp_detail` ON`dpp_master`.`du_id`=`dpp_detail`.`du_id` WHERE `dpp_master`.du_id=%s"
        val0 = (session['d2_id'])
        res0 = selectall2(qry0, val0)

        qry1="SELECT SUM(`quantity`*`price`) FROM `dpp_detail` WHERE du_id=%s"
        res1=selectone(qry1,val0)

        return render_template("dpublisherpurchase2.html",languages=languages,val=res0,p=res1[0],val1=res2)


@app.route('/addbook')
def addbook():
    d = datetime.now()
    d1 = d.date()
    print(d1)
    qry = "select * from section"
    res = selectall(qry)

    return render_template("addbook.html",val1=d1,val=res)

@app.route('/addstock')
def addstock():
    qry = "select * from section"
    res = selectall(qry)

    return render_template("addstock.html",val=res)



@app.route('/gcode',methods=['get','post'])
def gcode():
    genre = request.form['brand']
    print(genre,"******************************")
    qry = "SELECT book.* FROM book JOIN section on book.s_id=section.s_id where section.name=%s"
    val = (genre)
    res2 = selectall2(qry,val)
    print(res2)
    lis = [0, 'select']
    for r in res2:
        lis.append(r[0])
        lis.append(r[1])
    print(lis)
    resp = make_response(jsonify(lis))
    resp.status_code = 200
    resp.headers['Access-Control-Allow-Origin'] = '*'
    return resp


@app.route('/deleteitem')
def deleteitem():
    did=request.args.get('id')

    qry="delete from dup_detail where id=%s"
    val=(did)
    iud(qry,val)

    return '''<script>alert('Deleted item successfully');window.location='/duserpurchase2';</script>'''

@app.route('/deleteitem2')
def deleteitem2():
    did=request.args.get('id')

    qry="delete from dpp_detail where id=%s"
    val=(did)
    iud(qry,val)

    return '''<script>alert('Deleted item successfully');window.location='/dpublisherpurchase2';</script>'''


@app.route('/deletesection')
def deletesection():
    sid=request.args.get('id')

    qry="delete from section where s_id=%s"
    val=(sid)
    iud(qry,val)

    return '''<script>alert('Deleted successfully');window.location='/viewsection';</script>'''

@app.route('/deletebook')
def deletebook():
    bid=request.args.get('id')

    qry="Update book set status='Removed' where b_id=%s"
    val=(bid)
    iud(qry,val)
    return '''<script>alert('Removed successfully');window.location='/viewbook';</script>'''


@app.route('/deleteemployee')
def deleteemployee():
    eid=request.args.get('id')

    qry="delete from employee where l_id=%s"
    val=(eid)
    iud(qry,val)

    qry2 = "delete from login where l_id=%s"
    iud(qry2,val)

    return '''<script>alert('Deleted successfully');window.location='/viewemployee';</script>'''


@app.route('/updatesection')
def updatesection():
    sid=request.args.get('id')
    session['sid']=sid
    qry="select * from section where s_id=%s"
    val=(sid)
    res=selectone(qry,val)

    return render_template("updatesection.html",val=res)

@app.route('/updateemployee')
def updateemployee():
    eid=request.args.get('id')
    session['eid']=eid
    qry="select * from employee where l_id=%s"
    val=(eid)
    res=selectone(qry,val)

    return render_template("updateemployee.html",val=res)


@app.route('/viewbill')
def viewbill():
    qry0 = "SELECT * FROM book JOIN `dup_master` JOIN `dup_detail` ON `book`.`b_id`=`dup_detail`.`bid` AND `dup_master`.`dp_id`=`dup_detail`.`dp_id` WHERE `dup_master`.dp_id=%s"
    val0 = (session['d1_id'])
    res0 = selectall2(qry0, val0)

    qry1 = "SELECT SUM(`dup_detail`.`quantity`*`book`.`price`) FROM book JOIN `dup_master` JOIN `dup_detail` ON `book`.`b_id`=`dup_detail`.`bid` AND `dup_master`.`dp_id`=`dup_detail`.`dp_id` WHERE `dup_master`.dp_id=%s"
    res1 = selectone(qry1, val0)
    return render_template('bill.html',val1=res0,val2=res1)




@app.route('/updatebook')
def updatebook():

    qry = "select * from book where b_id=%s"
    val = (session['bid'])
    res = selectone(qry,val)

    qry2="SELECT * FROM `section`"

    res1=selectall(qry2)
    return render_template("updatebook.html", val=res,val1=res1,id=res[5])



@app.route('/updatebookcode',methods=['post'])
def updatebookcode():
    try:
        title = request.form['title']
        author = request.form['author']
        publisher = request.form['publisher']
        publisher_d = request.form['publisher_d']
        Genre = request.form['Genre']
        page = request.form['page']
        description = request.form['description']
        price = request.form['price']
        isbn = request.form['isbn']
        url = request.form['url']
        print(isbn)
        qry = "update `book` set `title`=%s,`author`=%s,`publisher`=%s,`p_year`=%s,`description`=%s,`pageno`=%s,`price`=%s,`url`=%s,`isbn`=%s, s_id=%s where b_id=%s"
        val = (title, author, publisher, publisher_d, description, page, price, url, isbn, Genre, session['bid'])
        iud(qry, val)
        return "<script>alert('Updated successfully');window.location='/viewbook2?id="+str(session['bid'])+"';</script>"

    except Exception as e:
        title = request.form['title']
        author = request.form['author']
        publisher = request.form['publisher']
        publisher_d = request.form['publisher_d']
        Genre = request.form['Genre']
        page = request.form['page']
        description = request.form['description']
        price = request.form['price']
        isbn = request.form['isbn']
        url = request.form['url']

        pic1 = request.files['pic1']
        pic1name = secure_filename(pic1.filename)
        pic1.save(os.path.join('static/bookpic', pic1name))

        pic2 = request.files['pic2']
        pic2name = secure_filename(pic2.filename)
        pic2.save(os.path.join('static/bookpic', pic2name))

        qry = "update `book` set `title`=%s,`author`=%s,`publisher`=%s,`p_year`=%s,`description`=%s,`pageno`=%s,`price`=%s,`pic1`=%s,`pic2`=%s,`url`=%s,`isbn`=%s, genre=%s where b_id=%s"
        val = (title, author, publisher, publisher_d, description, page, price, pic1name, pic2name, url, isbn,Genre, session['bid'])
        iud(qry, val)
        return '''<script>alert('Updated successfully');window.location='/viewbook2';</script>'''





@app.route('/updateemployeecode',methods=['post'])
def updateemployeecode():
    fname=request.form['fname']
    lname = request.form['lname']
    gender = request.form['radio-inline']
    hname = request.form['hname']
    place = request.form['place']
    post = request.form['post']
    pin = request.form['pin']
    phoneno = request.form['phoneno']
    emailid = request.form['emailid']


    qry2 = "UPDATE `employee` SET `fname`=%s,`lname`=%s,`gender`=%s,`hname`=%s,`place`=%s,`post`=%s,`pin`=%s,`phoneno`=%s,`emailid`=%s where l_id=%s"
    val2 = (fname,lname,gender,hname,place,post,pin,phoneno,emailid,session['eid'])
    iud(qry2,val2)
    return '''<script>alert('Updated successfully');window.location='/viewemployee';</script>'''



@app.route('/updatesectioncode',methods=['post'])
def updatesectioncode():
    genre = request.form['Genre']
    location = request.form['Location']
    qry = "Update `section` set name=%s, location=%s where s_id=%s"
    val = (genre,location,session['sid'])
    iud(qry,val)
    return '''<script>alert('Updated successfully');window.location='/viewsection';</script>'''

@app.route('/viewemployee')
def viewemployee():
    qry="SELECT * FROM `employee`"
    res=selectall(qry)
    return  render_template("viewemployee.html",val=res)


@app.route('/viewbook')
def viewbook():
    qry="SELECT * FROM book LEFT JOIN stockmanage ON book.b_id=stockmanage.b_id WHERE book.b_id NOT IN(SELECT b_id FROM book WHERE `status`='Removed') "
    res=selectall(qry)
    return  render_template("viewbook.html",val=res)


@app.route('/a_viewbook1')
def a_viewbook1():
    qry="select * from book left join stockmanage on book.b_id=stockmanage.b_id"
    res=selectall(qry)
    return  render_template("a_viewbook1.html",val=res)


@app.route('/a_viewbook2')
def a_viewbook2():
    bid = request.args.get('id')
    session['bid'] = bid
    qry = "SELECT * FROM `book` JOIN `section` on `book`.`s_id`=`section`.`s_id` where b_id=%s"
    val = (bid)
    res = selectone(qry,val)
    return  render_template("a_viewbook2.html",val=res)


@app.route('/viewbook2')
def viewbook2():
    bid = request.args.get('id')
    session['bid'] = bid
    qry = "SELECT * FROM `book` JOIN `section` on `book`.`s_id`=`section`.`s_id` where `book`.b_id=%s"
    val = (bid)
    res = selectone(qry,val)
    return  render_template("viewbook2.html",val=res)


@app.route('/viewsection')
def viewsection():
    qry="SELECT * FROM `section`"
    res=selectall(qry)
    return  render_template("viewsection.html",val=res)




@app.route('/addstockcode',methods=['post'])
def addstockcode():

    t = request.form['title']
    bno = request.form['bno']
    d = datetime.datetime.today()
    q = "select * from stockmanage where b_id=%s "
    v = (t)
    r = selectone(q,v)
    print(r)
    if r is None:
        qry = "insert into stock values (NULL,%s,%s,%s)"
        val = (t,d,bno)
        iud(qry,val)
        qry2 = "insert into stockmanage value (NULL,%s,%s)"
        val2 = (t,bno)
        iud(qry2,val2)
    else :
        qry = "insert into stock values (NULL,%s,%s,%s)"
        val = (t, d, bno)
        iud(qry, val)
        qry2 = "select * from stockmanage where b_id=%s"
        val2 = (t)
        res2 = selectone(qry2,val2)
        b = int(res2[2])+int(bno)
        qry3 = "Update stockmanage set noofbook=%s where b_id=%s  "
        val3 = (b,t)
        iud(qry3,val3)

    return '''<script>alert('Added successfully');window.location='/addstock';</script>'''


@app.route('/addbookcode',methods=['post'])
def addbookcode():
    title = request.form['title']
    author = request.form['author']
    publisher = request.form['publisher']
    publisher_d = request.form['publisher_d']
    genre = request.form['Genre']
    page = request.form['page']
    description = request.form['description']
    price = request.form['price']
    isbn = request.form['isbn']
    url = request.form['url']

    pic1 = request.files['pic1']
    pic1name = secure_filename(pic1.filename)
    pic1.save(os.path.join('static/bookpic',pic1name))

    pic2 = request.files['pic2']
    pic2name = secure_filename(pic2.filename)
    pic2.save(os.path.join('static/bookpic', pic2name))

    qry = "insert into book values(NULL,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,'Present')"
    val = (title,author,publisher,publisher_d,genre,description,page,price,pic1name,pic2name,url,isbn)
    iud(qry,val)
    return '''<script>alert('Added successfully');window.location='/addbook';</script>'''


@app.route('/addsectioncode',methods=['post'])
def addsectioncode():
    genre = request.form['Genre']
    location = request.form['Location']

    qry = "insert into section values (NULL,%s,%s)"
    val = (genre,location)
    iud(qry,val)
    return '''<script>alert('Added successfully');window.location='/addsection';</script>'''



@app.route('/addemployeecode',methods=['post'])
def addemployeecode():

    fname=request.form['fname']
    lname = request.form['lname']
    gender = request.form['radio-inline']
    hname = request.form['hname']
    place = request.form['place']
    post = request.form['post']
    pin = request.form['pin']
    phoneno = request.form['phoneno']
    emailid = request.form['emailid']
    uname = request.form['uname']
    password = request.form['password']
    cpswd=request.form['password2']
    if cpswd==password:


        qry1 = "insert into login values (NULL,%s,%s,'Employee')"
        val1 = (uname,password)
        lid = iud(qry1,val1)

        qry2 = "insert into employee values (NULL,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s)"
        val2 = (fname,lname,gender,hname,place,post,pin,phoneno,emailid,lid)
        iud(qry2,val2)
        return '''<script>alert('Added successfully');window.location='/addemployee';</script>'''
    else:
        return '''<script>alert('Confirm password does not match');window.location='/addemployee';</script>'''


app.run(debug=True)