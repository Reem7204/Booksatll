from flask import *
from src.dbconnection import *
import os
import datetime
import requests
import smtplib
from datetime import datetime,timedelta
from werkzeug.utils import secure_filename
from src.classifier import CLASSIFIER
from email.mime.text import MIMEText
from flask_mail import Mail
app = Flask(__name__)
app.secret_key="sdsd"
import functools

def login_required(func):
    @functools.wraps(func)
    def secure_function():
        if "lid" not in session:
            return render_template('login1.html')
        return func()
    return secure_function


@app.route('/')
def first():
    return render_template("index.html")


@app.route('/login')
def login():
    return render_template("login1.html")


@app.route('/report')
@login_required
def report():
    q12 = "SELECT SUM(noofbook) FROM stockmanage"
    r12 = selectone2(q12)
    return render_template("report.html",val1="",val2="",val3="",val4="",val8="",val9="",val10="",val11="",val12=r12)


@app.route('/report2',methods=['post'])
@login_required
def report2():
    d1 = request.form['d1']
    d2 = request.form['d2']
    if d1<d2:
            q1 = "SELECT CAST(SUM(`book`.`price`*`userpd`.`quantity`) AS DECIMAL(10,2)) FROM `userpm` JOIN `userpd` JOIN `book` ON `book`.`b_id`=`userpd`.`b_id` AND `userpd`.`pm_id`=`userpm`.`pm_id` WHERE `userpm`.`date` BETWEEN %s AND %s"
            v1 = (d1,d2)
            r1 = selectone(q1,v1)
            if r1[0] is None:
                r_1=0
            else:
                r_1=r1[0]
            q2 = "SELECT CAST(SUM(amount) AS DECIMAL(10,2)) FROM expense WHERE DATE BETWEEN %s AND %s"
            r2 = selectone(q2,v1)
            q5 = "SELECT CAST(SUM(`p_book`.`price`*`paddcart`.`qty`) AS DECIMAL(10,2)) FROM `paddcart` JOIN `p_book` ON `p_book`.`id`=`paddcart`.`bookid` WHERE DATE BETWEEN %s AND %s AND `paddcart`.`status`='Accepted'"
            r5 = selectone(q5,v1)
            q8 = "SELECT CAST(SUM(`dpp_detail`.`price`*`dpp_detail`.`quantity`) AS DECIMAL(10,2)) FROM `dpp_master` JOIN `dpp_detail` ON `dpp_detail`.`du_id`=`dpp_master`.`du_id` WHERE `dpp_master`.`date` BETWEEN %s AND %s"
            r8 = selectone(q8,v1)
            print(r2)
            print(r5)
            print(r8)
            try:
                if r2[0] is None or r5[0] is None or r8[0] is None:
                    r=0
                    r3=0
                else:
                    r = r2[0] + r5[0] +r8[0]
                    print(r)
                    r3 = r1[0] - r
            except:
                r = 0
                r3 = 0

            q4 = "SELECT SUM(quantity) FROM stock WHERE DATE BETWEEN %s AND %s"
            r4 = selectone(q4,v1)
            if r4[0] is None:
                r_4=0
            else:
                r_4=r4[0]
            q6 = "SELECT SUM(quantity) FROM `userpm` JOIN `userpd` ON `userpm`.`pm_id`=`userpd`.`pm_id` WHERE DATE BETWEEN %s AND %s AND `status` IN ('Accepted','Delivered')"
            r6 = selectone(q6,v1)
            q7 = "SELECT SUM(quantity) FROM `dup_master` JOIN `dup_detail` ON `dup_master`.`dp_id`=`dup_detail`.`dp_id` WHERE DATE BETWEEN %s AND %s"
            r7 = selectone(q7,v1)
            try:
                if r6[0] is None or r7[0] is None:
                    r8=0
                else:
                    r8 = r6[0]+r7[0]
            except:
               r8=0
            q9 = "SELECT COUNT(pm_id) FROM `userpm` WHERE DATE BETWEEN %s AND %s"
            r9 = selectone(q9,v1)
            q10 = "SELECT COUNT(pm_id) FROM `userpm` WHERE `status` in ('Accepted','Cancelled') and DATE BETWEEN %s AND %s"
            r10 = selectone(q10,v1)
            q11 = "SELECT COUNT(pm_id) FROM `userpm` WHERE `status`='Cancelled' and DATE BETWEEN %s AND %s"
            r11 = selectone(q11,v1)
            q12 = "SELECT SUM(noofbook) FROM stockmanage"
            r12 = selectone2(q12)
            print(r12)
            return render_template("report.html",val1=r_1,val2=r,val3=r3,val4=r_4,val8=r8,val9=r9,val10=r10,val11=r11,val12=r12)
    else:
        return '''<script>alert('Invalid date');window.location='/report';</script>'''



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
        session['lid']=s[0]
        return redirect('/adminhome')
    elif s[3]=="Employee":
        session['lid']=s[0]
        return redirect('/employeehome')
    else:
        return '''<script>alert('Invalid username and password');window.location='/login';</script>'''



@app.route('/blankpage')
@login_required
def blankpage():

    return render_template("blankpage.html")

@app.route('/adminhome')
@login_required
def adminhome():
    qry = "SELECT count(*) FROM `book` JOIN `userpd` ON `userpd`.`b_id`=`book`.`b_id` JOIN `userpm` ON `userpd`.`pm_id`=`userpm`.`pm_id` JOIN `customer` ON `customer`.`l_id`=`userpm`.`cust_id` where userpm.status='Requested' ORDER BY `date` DESC"
    res = selectone2(qry)
    order_req=str(res[0])
    session['req']=order_req
    print(order_req,"=================================================")
    qry2 = "SELECT count(*) FROM `book` JOIN `userpd` ON `userpd`.`b_id`=`book`.`b_id` JOIN `userpm` ON `userpd`.`pm_id`=`userpm`.`pm_id` JOIN `customer` ON `customer`.`l_id`=`userpm`.`cust_id` where userpm.status='Cancel' ORDER BY `date` DESC"
    res2 = selectone2(qry2)
    cl_req = str(res2[0])
    session['req2'] = cl_req
    return render_template("adminhome.html")

@app.route('/employeehome')
@login_required
def employeehome():
    return render_template("employeehome.html")





@app.route('/addcartbook')
@login_required
def addcartbook():
    q = request.args.get('quantity')
    d = datetime.now().strftime("%Y-%m-%d")
    qry = "Insert into `paddcart` values (NULL,%s,%s,%s,'Requested','-')"
    val = (session['pb_id'],q,d)
    iud(qry,val)

    return '''<script>alert('Requested successfully');window.location='/pbook';</script>'''



@app.route('/pviewbook')
@login_required
def pviewbook():
    id = request.args.get('id')
    session['pb_id'] = id
    qry = "SELECT * FROM `p_book` JOIN `publisher` ON `publisher`.`lid`=`p_book`.`p_id` where p_book.id=%s"
    val = (id)
    res = selectone(qry,val)
    print(res)
    return render_template("pviewbook.html",val=res)


@app.route('/addexpense')
@login_required
def addexpense():
    d = datetime.now()
    d1 = d.date()
    print(d1)

    return render_template("addexpense.html", val1=d1)


@app.route('/addsection')
@login_required
def addsection():
    return render_template("addsection.html")




@app.route('/addemployee')
@login_required
def addemployee():
    d = datetime.now()
    d1 = d.date()
    print(d1)
    return render_template("addemployee.html",val1=d1)

@app.route('/addpurchasebook')
@login_required
def addpurchasebook():
    id = request.args.get('id')
    d = datetime.now()

    qry = "insert into dpp_master(`d_p`,date,`status`) values (%s,%s,'pending')"
    val = (id,d)
    i = iud(qry,val)
    session['d4_id'] = i
    print(id)
    qry = "select * from section"
    res = selectall(qry)
    return render_template("dpublisherpurchase2.html",val1=res)


@app.route('/dpublisherpurchase')
@login_required
def dpublisherpurchase():
    qry = "select * from section"
    res = selectall(qry)
    return render_template("dpublisherpurchase.html",val1=res)






@app.route('/dpublisherpurchase3', methods=["POST", "GET"])
@login_required
def dpublisherpurchase3():

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


@app.route('/dpuinsert')
@login_required
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

            qry6 = "select * from book join stockmanage on book.b_id=stockmanage.b_id where book.title=%s"
            val6 = (title)
            res6 = selectone(qry6,val6)
            if int(quantity)<res6[16]:
                print(int(quantity))
                a=res6[16]-int(quantity)

                qry7 = "update`stockmanage` set noofbook=%s where b_id=%s"
                val7 = (a,res6[15])
                iud(qry7,val7)
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
            else:
                return '''<script>alert('Out of Stock');window.location='/duserpurchase';</script>'''


        elif res[1]=='pending':


            title = request.args.get('title')

            quantity = request.args.get('quantity')
            qry6 = "select * from book join stockmanage on book.b_id=stockmanage.b_id where book.title=%s"
            val6 = (title)
            res6 = selectone(qry6, val6)
            if int(quantity) < res6[16]:
                a = res6[16] - int(quantity)

                qry7 = "update`stockmanage` set noofbook=%s where b_id=%s"
                val7 = (a, res6[15])
                iud(qry7, val7)

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
                return '''<script>alert('Out of Stock');window.location='/duserpurchase2';</script>'''
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
@login_required
def dpuinsert1():
    b = request.args.get('Submit')
    print(b)
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

            q = "select * from d_publisher where emailid=%s"
            r = selectone(q,emailid)

            if r is None:
                qry2 = "INSERT INTO `d_publisher` (`name`,`address`,`phoneno`,`emailid`) VALUES (%s,%s,%s,%s)"
                val2 = (name,address,phno,emailid)
                id1 = iud(qry2, val2)


                qry4 = "insert into `dpp_master` (`d_p`,`date`,`status`) values (%s,%s,'pending')"
                val4 = (id1,d)
                id = iud(qry4,val4)
                session['d4_id'] = id
                qry3 = "INSERT INTO `dpp_detail` (`id`,`du_id`,`isbn`,`s_id`,`title`,`author`,`price`,`quantity`) VALUES (NULL,%s,%s,%s,%s,%s,%s,%s)"
                val3 = (id,isbn,genre,title,author,price,quantity)
                session['d2_id'] = id
                d2 = iud(qry3, val3)

                return '''<script>alert('Item entered');window.location='/dpublisherpurchase2';</script>'''
            else:
                return '''<script>alert('Publisher already present');window.location='/dpublisherpurchase';</script>'''


        elif res[1]=='pending':

            title = request.args.get('title')
            quantity = request.args.get('quantity')
            isbn = request.args.get('isbn')
            genre = request.args.get('genre')
            author = request.args.get('author')
            price = request.args.get('price')

            # q = "SELECT * FROM `dpp_master` WHERE `status`='pending' AND `du_id` IN(SELECT MAX(du_id) FROM `dpp_master`)"
            #
            # r = selectone2(q)
            # print(r)
            r = session['d4_id']
            qry3 = "INSERT INTO `dpp_detail` (`id`,`du_id`,`isbn`,`s_id`,`title`,`author`,`price`,`quantity`) VALUES (NULL,%s,%s,%s,%s,%s,%s,%s)"
            val3 = (r,isbn,genre,title,author,price,quantity)

            print(r)
            iud(qry3,val3)

            return '''<script>alert('Item entered');window.location='/dpublisherpurchase2';</script>'''
        else:
            return '''<script>alert('Error');window.location='/dpublisherpurchase';</script>'''
    # elif b == "Edit":
    #     id = request.args.get('id')
    #     q = request.args.get('qty')
    #     print(id)
    #     qry = "Update dpp_detail set `quantity`=%s where id=%s"
    #     val = (q, id)
    #     iud(qry, val)
    #     return '''<script>alert('Item updated successfully');window.location='/dpublisherpurchase2';</script>'''
    elif b=="Save":
        qry6 = "select MAX(du_id) from dpp_master"
        res6 = selectone2(qry6)
        qry5 = "Update dpp_master set `status`='finished' where du_id=%s"
        val5 = (res6[0])
        print(res6[0])
        iud(qry5,val5)
        return '''<script>alert('Saved successfully');window.location='/dpublisherpurchase';</script>'''

@app.route('/edititem2')
@login_required
def edititem2():
    id = request.args.get('id')
    session['editppid']=id
    qry = "SELECT * FROM `dpp_detail` JOIN `section` ON `dpp_detail`.`s_id`=`section`.`s_id` WHERE `dpp_detail`.`id`=%s"
    val = (id)
    res = selectall2(qry,val)
    qry1 = "select * from section"
    res1 = selectall(qry1)
    return render_template("editpp.html",val=res,val1=res1,id=res[0][3])

@app.route('/edititem3')
@login_required
def edititem3():
    i = request.args.get('isbn')
    g = request.args.get('genre')
    t = request.args.get('title')
    a = request.args.get('author')
    p = request.args.get('price')
    q = request.args.get('quantity')
    print(q)
    qry = "Update dpp_detail set isbn=%s,s_id=%s,title=%s,author=%s,price=%s,`quantity`=%s where id=%s"
    val = (i,g,t,a,p,q,session['editppid'])
    iud(qry, val)
    return '''<script>alert('Item updated successfully');window.location='/dpublisherpurchase2';</script>'''

@app.route('/edititem4')
@login_required
def edititem4():
    id = request.args.get('id')
    session['edititem']=id
    qry = "SELECT * FROM `dup_detail` JOIN `book` ON `dup_detail`.`bid`=`book`.`b_id` WHERE `dup_detail`.`id`=%s"
    val = (id)
    res = selectone(qry,val)

    return render_template("qty2.html",val=res)

@app.route('/edititem5')
@login_required
def edititem5():

    q = request.args.get('quantity')
    print(q)
    qry = "Update dup_detail set `quantity`=%s where id=%s"
    val = (q,session['edititem'])
    iud(qry, val)
    return '''<script>alert('Item updated successfully');window.location='/duserpurchase2';</script>'''

@app.route('/bookdetails',methods=['get','post'])
@login_required
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
@login_required
def duserpurchase():

    if request.method == "GET":
        qry = "select * from book"
        languages=selectall(qry)

        return render_template("s.html",languages=languages)



@app.route('/duserpurchase2', methods=["POST", "GET"])
@login_required
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
@login_required
def dpublisherpurchase2():

    if request.method == "GET":
        qry2 = "select * from section"
        res2 = selectall(qry2)

        qry = "select * from book"
        languages=selectall(qry)

        qry0 = "SELECT * FROM `dpp_master` JOIN `dpp_detail` ON`dpp_master`.`du_id`=`dpp_detail`.`du_id` WHERE `dpp_master`.du_id=%s"
        val0 = (session['d4_id'])
        res0 = selectall2(qry0, val0)

        qry1="SELECT SUM(`quantity`*`price`) FROM `dpp_detail` WHERE du_id=%s"
        res1=selectone(qry1,val0)

        return render_template("dpublisherpurchase2.html",languages=languages,val=res0,p=res1[0],val1=res2)


@app.route('/addbook')
@login_required
def addbook():
    d = datetime.now()
    d1 = d.date()
    print(d1)
    qry = "select * from section"
    res = selectall(qry)

    return render_template("addbook.html",val1=d1,val=res)

@app.route('/addstock')
@login_required
def addstock():
    qry = "select * from section"
    res = selectall(qry)

    return render_template("addstock.html",val=res)



@app.route('/gcode',methods=['get','post'])
@login_required
def gcode():
    genre = request.form['brand']

    qry = "SELECT book.* FROM book JOIN section on book.s_id=section.s_id where section.name=%s and `book`.`status`!='Removed'"
    val = (genre)
    res2 = selectall2(qry,val)

    lis = [0, 'select']
    for r in res2:
        lis.append(r[0])
        lis.append(r[1])

    resp = make_response(jsonify(lis))
    resp.status_code = 200
    resp.headers['Access-Control-Allow-Origin'] = '*'
    return resp


@app.route('/deleteitem')
@login_required
def deleteitem():
    did=request.args.get('id')
    print(did)
    qry="delete from dup_detail where id=%s"
    val=(did)
    iud(qry,val)


    return '''<script>alert('Deleted item successfully');window.location='/duserpurchase2';</script>'''








@app.route('/deleteitem2')
@login_required
def deleteitem2():
    did=request.args.get('id')

    qry="delete from dpp_detail where id=%s"
    val=(did)
    iud(qry,val)

    return '''<script>alert('Deleted item successfully');window.location='/dpublisherpurchase2';</script>'''


@app.route('/deletesection')
@login_required
def deletesection():
    sid=request.args.get('id')

    qry="delete from section where s_id=%s"
    val=(sid)
    iud(qry,val)

    return '''<script>alert('Deleted successfully');window.location='/viewsection';</script>'''


@app.route('/deletebook')
@login_required
def deletebook():
    bid=request.args.get('id')

    qry="Update book set status='Removed' where b_id=%s"
    val=(bid)
    iud(qry,val)
    return '''<script>alert('Removed successfully');window.location='/viewbook';</script>'''


@app.route('/deleteemployee')
@login_required
def deleteemployee():
    eid=request.args.get('id')
    d = datetime.now()
    d1 = d.date()
    qry0 = "select * from employee where l_id=%s and status='Resigned'"
    res0 = selectone(qry0,eid)
    if res0 is None:
        qry="Update employee set r_date=%s ,status='Resigned'  where l_id=%s"
        val=(d1,eid)
        iud(qry,val)

        qry2 = "delete from login where l_id=%s"
        iud(qry2,eid)

        return '''<script>alert('Resigned successfully');window.location='/viewemployee';</script>'''
    else:
        return '''<script>alert('Already resigned');window.location='/viewemployee';</script>'''


@app.route('/updatesection')
@login_required
def updatesection():
    sid=request.args.get('id')
    session['sid']=sid
    qry="select * from section where s_id=%s"
    val=(sid)
    res=selectone(qry,val)

    return render_template("updatesection.html",val=res)


@app.route('/replycomplaint')
@login_required
def replycomplaint():
    sid=request.args.get('id')
    session['rid']=sid
    qry="select * from complaint where id=%s"
    val=(sid)
    res=selectone(qry,val)
    return render_template("replycomplaint.html", val=res)


@app.route('/updateexpense')
@login_required
def updateexpense():
    sid=request.args.get('id')
    session['sid2']=sid
    qry="select * from expense where id=%s"
    val=(sid)
    res=selectone(qry,val)

    return render_template("updateexpense.html",val=res)


@app.route('/updateemployee')
@login_required
def updateemployee():
    eid=request.args.get('id')
    session['eid']=eid
    qry="select * from employee where l_id=%s"
    val=(eid)
    res=selectone(qry,val)

    return render_template("updateemployee.html",val=res)


@app.route('/viewbill',methods=['post','get'])
@login_required
def viewbill():
    qry0 = "SELECT * FROM book JOIN `dup_master` JOIN `dup_detail` ON `book`.`b_id`=`dup_detail`.`bid` AND `dup_master`.`dp_id`=`dup_detail`.`dp_id` WHERE `dup_master`.dp_id=%s"
    val0 = (session['d1_id'])
    res0 = selectall2(qry0, val0)

    qry1 = "SELECT SUM(`dup_detail`.`quantity`*`book`.`price`) FROM book JOIN `dup_master` JOIN `dup_detail` ON `book`.`b_id`=`dup_detail`.`bid` AND `dup_master`.`dp_id`=`dup_detail`.`dp_id` WHERE `dup_master`.dp_id=%s"
    res1 = selectone(qry1, val0)
    return render_template('bill.html',val1=res0,val2=res1)




@app.route('/updatebook')
@login_required
def updatebook():

    qry = "select * from book where b_id=%s"
    val = (session['bid'])
    res = selectone(qry,val)

    qry2="SELECT * FROM `section`"

    res1=selectall(qry2)
    return render_template("updatebook.html", val=res,val1=res1,id=res[5])



@app.route('/comment')
@login_required
def comment():
    bid = request.args.get('id')

    qry = "SELECT * FROM `review` JOIN `customer` ON `review`.`cust_id`=`customer`.`l_id` WHERE `review`.`bid`=%s"
    val = (bid)
    res = selectall2(qry,val)
    print(res)

    return render_template("comment.html", val=res)

@app.route('/comment2')
@login_required
def comment2():
    bid = request.args.get('id')

    qry = "SELECT * FROM `review` JOIN `customer` ON `review`.`cust_id`=`customer`.`l_id` WHERE `review`.`bid`=%s"
    val = (bid)
    res = selectall2(qry,val)
    print(res)

    return render_template("comment2.html", val=res)

@app.route('/editbook')
@login_required
def editbook():
    bid = request.args.get('id')
    session['b']=bid
    qry = "select * from book join stockmanage on book.b_id=stockmanage.b_id where stockmanage.sm_id=%s"
    val = (bid)
    res = selectone(qry,val)
    print(res)

    return render_template("qty.html", val=res)


@app.route('/editbook1')
@login_required
def editbook1():

    qty = request.args.get('quantity')
    qry = "update stockmanage set noofbook=%s where sm_id=%s"
    val = (qty,session['b'])
    iud(qry,val)
    return '''<script>alert('Updated quantity');window.location='/viewbook';</script>'''



@app.route('/updatebookcode',methods=['post'])
@login_required
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
@login_required
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



@app.route('/addcomplaint',methods=['post'])
@login_required
def addcomplaint():
    r=request.form['reply']
    qry2 = "UPDATE `complaint` SET `reply`=%s where id=%s"
    val2 = (r,session['rid'])
    iud(qry2,val2)
    return '''<script>alert('Reply send');window.location='/complaint';</script>'''



@app.route('/updatesectioncode',methods=['post'])
@login_required
def updatesectioncode():
    genre = request.form['Genre']
    location = request.form['Location']
    qry2 = "Select * from section where name=%s"
    val = (genre)
    res = selectone(qry2, val)

    if res is None:
        qry = "Update `section` set name=%s, location=%s where s_id=%s"
        val = (genre,location,session['sid'])
        iud(qry,val)
        return '''<script>alert('Updated successfully');window.location='/viewsection';</script>'''
    else:
        return '''<script>alert('Genre already present');window.location='/viewsection';</script>'''



@app.route('/updateexpensecode',methods=['post'])
@login_required
def updateexpensecode():
    date = request.form['date']
    amount = request.form['amount']
    description = request.form['description']

    qry = "update expense set date=%s, amount=%s, description=%s where id=%s"
    val = (date,amount,description,session['sid2'])
    iud(qry, val)
    return '''<script>alert('Updated successfully');window.location='/viewexpense';</script>'''



@app.route('/viewemployee')
@login_required
def viewemployee():
    qry="SELECT * FROM `employee`"
    res=selectall(qry)
    return  render_template("viewemployee.html",val=res)


@app.route('/acceptorder')
@login_required
def acceptorder():
    id = request.args.get('id')
    q = "SELECT * FROM `userpd` WHERE pm_id=%s"
    r = selectone(q,id)
    print(r)
    q1 = "SELECT * FROM `stockmanage` WHERE b_id=%s"
    r1 = selectone(q1,r[2])
    print(r1)
    if ( r[3]<r1[2] ) :
        s = r1[2]-r[3]
        q3 = "update stockmanage set noofbook=%s where b_id=%s"
        v3 = (s,r[2])
        iud(q3,v3)
        qry="Update userpm set status='Accepted' where pm_id=%s"
        iud(qry,id)
        return '''<script>alert('Accepted successfully');window.location='/userrequest';</script>'''
    else:
        return '''<script>alert('Out of stock');window.location='/userrequest';</script>'''


@app.route('/acceptcancel')
@login_required
def acceptcancel():
    id = request.args.get('id')
    q = "SELECT * FROM `userpd` WHERE pm_id=%s"
    r = selectone(q,id)
    print(r)
    q1 = "SELECT * FROM `stockmanage` WHERE b_id=%s"
    r1 = selectone(q1,r[2])
    print(r1)

    s = r1[2]+r[3]
    q3 = "update stockmanage set noofbook=%s where b_id=%s"
    v3 = (s,r[2])
    iud(q3,v3)
    qry="Update userpm set status='Cancelled' where pm_id=%s"
    iud(qry,id)
    return '''<script>alert('Cancel accepted');window.location='/cancelrequest';</script>'''


@app.route('/rejectorder')
@login_required
def rejectorder():
    id = request.args.get('id')
    qry="Update userpm set status='Rejected' where pm_id=%s"
    iud(qry,id)
    return '''<script>alert('Rejected successfully');window.location='/userrequest';</script>'''


@app.route('/ordercancel')
@login_required
def ordercancel():
    id = request.args.get('id')
    q = "SELECT * FROM `paddcart` WHERE id=%s"
    r = selectone(q,id)
    print(r)
    if r[4]=='Requested':
        q2 = "delete from paddcart where id=%s"
        iud(q2,id)
        return '''<script>alert('Cancelled');window.location='/requeststatus';</script>'''
    elif r[4]=='Accepted':
        qry = "Update `paddcart` set status='Cancel' where id=%s"
        iud(qry, id)
        return '''<script>alert('Cancelled');window.location='/requeststatus';</script>'''
    else:
        return '''<script>alert('Already cancelled');window.location='/requeststatus';</script>'''


@app.route('/delivered')
@login_required
def delivered():
    id = request.args.get('id')
    qry = "Update `userpm` set status='Delivered' where pm_id=%s"
    iud(qry, id)
    return '''<script>alert('Status changed');window.location='/acceptedorders';</script>'''



@app.route('/userrequest')
@login_required
def userrequest():
    qry="SELECT *,DATE_FORMAT(userpm.date,'%d-%m-%Y') FROM `book` JOIN `userpd` ON `userpd`.`b_id`=`book`.`b_id` JOIN `userpm` ON `userpd`.`pm_id`=`userpm`.`pm_id` JOIN `customer` ON `customer`.`l_id`=`userpm`.`cust_id` where userpm.status='Requested' ORDER BY `date` DESC"
    res=selectall(qry)
    return  render_template("userrequest.html",val=res)



@app.route('/acceptedorders')
@login_required
def acceptedorders():
    qry="SELECT *,DATE_FORMAT(userpm.date,'%d-%m-%Y') FROM `book` JOIN `userpd` ON `userpd`.`b_id`=`book`.`b_id` JOIN `userpm` ON `userpd`.`pm_id`=`userpm`.`pm_id` JOIN `customer` ON `customer`.`l_id`=`userpm`.`cust_id` where userpm.status='Accepted' ORDER BY `date` DESC"
    res=selectall(qry)
    return  render_template("Acceptedorder.html",val=res)


@app.route('/cancelrequest')
@login_required
def cancelrequest():
    qry="SELECT *,DATE_FORMAT(userpm.date,'%d-%m-%Y') FROM `book` JOIN `userpd` ON `userpd`.`b_id`=`book`.`b_id` JOIN `userpm` ON `userpd`.`pm_id`=`userpm`.`pm_id` JOIN `customer` ON `customer`.`l_id`=`userpm`.`cust_id` where userpm.status='Cancel' ORDER BY `date` DESC"
    res=selectall(qry)
    return  render_template("cancelrequest.html",val=res)



@app.route('/orderhistory')
@login_required
def orderhistory():
    qry="SELECT *,DATE_FORMAT(userpm.date,'%d-%m-%Y') FROM `book` JOIN `userpd` ON `userpd`.`b_id`=`book`.`b_id` JOIN `userpm` ON `userpd`.`pm_id`=`userpm`.`pm_id` JOIN `customer` ON `customer`.`l_id`=`userpm`.`cust_id` ORDER BY `date` DESC"
    res=selectall(qry)
    return  render_template("orderhistory.html",val=res)



@app.route('/mypublisher')
@login_required
def mypublisher():
    qry="SELECT * FROM `d_publisher`"
    res=selectall(qry)
    return  render_template("mypublisher.html",val=res)


@app.route('/viewbook')
@login_required
def viewbook():
    qry="SELECT * FROM book LEFT JOIN stockmanage ON book.b_id=stockmanage.b_id WHERE book.b_id NOT IN(SELECT b_id FROM book WHERE `status`='Removed')"
    res=selectall(qry)
    return  render_template("viewbook.html",val=res)


@app.route('/a_viewbook1')
@login_required
def a_viewbook1():
    qry="select * from book left join stockmanage on book.b_id=stockmanage.b_id WHERE book.b_id NOT IN(SELECT b_id FROM book WHERE `status`='Removed')"
    res=selectall(qry)
    return  render_template("a_viewbook1.html",val=res)


@app.route('/requeststatus')
@login_required
def requeststatus():
    qry="SELECT *,DATE_FORMAT(paddcart.date,'%d-%m-%Y'),DATE_FORMAT(paddcart.d_date,'%d-%m-%Y') FROM `paddcart` JOIN `p_book` ON `p_book`.`id`=`paddcart`.`bookid` ORDER BY `date` DESC"
    res=selectall(qry)
    return  render_template("requeststatus.html",val=res)


@app.route('/a_viewbook2')
@login_required
def a_viewbook2():
    bid = request.args.get('id')
    session['bid'] = bid
    qry = "SELECT `book`.*,section.*,DATE_FORMAT(book.p_year,'%d-%m-%Y'),ROUND(AVG(`crawlresult`.`rating`),1) FROM `book` JOIN `section` ON `book`.`s_id`=`section`.`s_id` join crawlresult on `book`.`b_id`=`crawlresult`.`product_id` WHERE `book`.b_id='"+str(bid)+"'"

    res = selectone2(qry)
    return  render_template("a_viewbook2.html",val=res)

@app.route('/vbook2')
@login_required
def vbook2():
    bid = request.args.get('id')

    qry = " SELECT * FROM p_book JOIN `publisher` ON `p_book`.`p_id`=`publisher`.`lid` WHERE `p_book`.`id`=%s"

    res = selectone(qry, bid)
    return  render_template("rs.html",val=res)


@app.route('/cust_detail')
@login_required
def cust_detail():
    id = request.args.get('id')
    qry = "Select * from customer where l_id=%s"
    res = selectone(qry,id)
    return  render_template("cust_detail.html",val=res)


@app.route('/s_pbook')
@login_required
def s_pbook():
    s = request.args.get('S')
    qry = "SELECT `p_book`.*,`publisher`.`name` FROM `p_book` JOIN `publisher` ON `publisher`.`lid`=`p_book`.`p_id` where title like '%"+str(s)+"%' or author like '%"+str(s)+"%'"
    res = selectall(qry)
    return  render_template("pbook.html",val=res)


@app.route('/pbook')
@login_required
def pbook():
    qry = "SELECT `p_book`.*,`publisher`.`name` FROM `p_book` JOIN `publisher` ON `publisher`.`lid`=`p_book`.`p_id`"
    res = selectall(qry)
    print(res)
    return render_template("pbook.html",val=res)




@app.route('/suggestion')
@login_required
def suggestion():

    qry = "SELECT *,DATE_FORMAT(suggestion.date,'%d-%m-%Y') from suggestion join customer where suggestion.cust_id=customer.l_id order by date desc"
    res = selectall(qry)
    return  render_template("suggestion.html",val=res)


@app.route('/complaint')
@login_required
def complaint():

    qry = "SELECT *,DATE_FORMAT(complaint.date,'%d-%m-%Y') from complaint join customer where complaint.cust_id=customer.l_id order by date desc"
    res = selectall(qry)
    return  render_template("complaint.html",val=res)




@app.route('/rs')
@login_required
def rs():
    bid = request.args.get('id')

    qry = " SELECT * FROM p_book JOIN `publisher` ON `p_book`.`p_id`=`publisher`.`lid` WHERE `p_book`.`id`=%s"

    res = selectone(qry,bid)
    return  render_template("rs.html",val=res)


@app.route('/viewbook2')
@login_required
def viewbook2():
    bid = request.args.get('id')
    session['bid'] = bid
    qry = "SELECT `book`.*,section.*,DATE_FORMAT(book.p_year,'%d-%m-%Y'),ROUND(AVG(`crawlresult`.`rating`),1) FROM `book` JOIN `section` ON `book`.`s_id`=`section`.`s_id` join crawlresult on `book`.`b_id`=`crawlresult`.`product_id` WHERE `book`.b_id='"+str(bid)+"'"
    res = selectone2(qry)
    return  render_template("viewbook2.html",val=res)


@app.route('/viewsection')
@login_required
def viewsection():
    qry="SELECT * FROM `section`"
    res=selectall(qry)
    return  render_template("viewsection.html",val=res)

@app.route('/viewexpense')
@login_required
def viewexpense():
    qry="SELECT *,DATE_FORMAT(expense.date,'%d-%m-%Y') FROM `expense`"
    res=selectall(qry)
    return  render_template("viewexpense.html",val=res)


@app.route('/viewsection2')
@login_required
def viewsection2():
    qry="SELECT * FROM `section`"
    res=selectall(qry)
    return  render_template("viewsection2.html",val=res)




@app.route('/addstockcode',methods=['post'])
@login_required
def addstockcode():

    t = request.form['title']
    bno = request.form['bno']
    d = datetime.now()
    print(t)
    if t != '0':
        q = "select * from stockmanage where b_id=%s "
        v = (t)
        r = selectone(q,v)

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
    else:
        return '''<script>alert('Select a book title');window.location='/addstock';</script>'''


@app.route('/addbookcode',methods=['post'])
@login_required
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

    qry1 = "Select * from book where `isbn`=%s"
    val1 = (isbn)
    res = selectall2(qry1,val1)
    print(res)
    if res is ():
        pic1 = request.files['pic1']
        pic1name = secure_filename(pic1.filename)
        pic1.save(os.path.join('static/bookpic', pic1name))

        pic2 = request.files['pic2']
        pic2name = secure_filename(pic2.filename)
        pic2.save(os.path.join('static/bookpic', pic2name))

        qry = "insert into book values(NULL,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,'Present')"
        val = (title, author, publisher, publisher_d, genre, description, page, price, pic1name, pic2name, url, isbn)
        bid = iud(qry, val)

        avgrating = []
        # amazonlink = request.form['url']
        # pname = bid
        print(url, "ressssssssssssssssssssssssss")
        # cmd.execute("INSERT INTO `product_url` VALUES(NULL,'" + pname + "','" + amazonlink + "','0')")
        # pid = con.insert_id()
        res1 = requests.get(url)
        # print(res1)
        # print(res1.text)
        resn = []
        row = []
        # print(res1.text)
        import re
        clean = re.compile('<.*?>')
        result = res1.text.split('<div class="t-ZTKy">')
        print(len(result), "=============")
        lis = []
        cnt = 0
        for i in range(1, len(result)):
            try:
                review = result[i].split('</div>')[0]
                review = re.sub(clean, " ", review).replace("\n", " ")
                print(review, "===========================================")
                lis.append(review)
                cnt = cnt + len(lis)
                print(cnt, "cnttt*******************************************")
                from nltk.sentiment.vader import SentimentIntensityAnalyzer
                pstv = 0
                ngtv = 0
                ntl = 0
                sid = SentimentIntensityAnalyzer()
                ss = sid.polarity_scores(review)
                a = float(ss['pos'])
                b = float(ss['neg'])
                c = float(ss['neu'])
                rating = 2.5
                if (ss['neu'] > ss['pos'] and ss['neu'] > ss['neg']):
                    pass
                if (ss['neg'] > ss['pos']):
                    negva = 5 - (5 * ss['neg'])
                    if negva > 2.5:
                        negva = negva - 2.5
                    rating = negva
                else:
                    negva = 5 * ss['pos']
                    if negva < 2.5:
                        negva = negva + 2.5
                    rating = negva

                avgrating.append(rating)


                import re
                cl = re.compile('\W')
                review = re.sub(cl, " ", review)
                # cmd.execute(
                #     "INSERT INTO `review` VALUES(NULL,'" + str(pid) + "','" + str(review) + "','" + str(rating) + "')")
                # con.commit()
                qry2 = "Insert into crawlresult values (NULL,%s,%s,%s)"
                val2 = (bid,review,rating)
                iud(qry2,val2)
            except Exception as e:
                print("looooooooooooooooooooo++++++++++++++++++++++++", e)
        for ii in range(2, 10):
            res1 = requests.get(url + "&page=" + str(ii))
            print(res1)
            # print(res1.text)
            resn = []
            row = []
            # print(res1.text)
            import re
            clean = re.compile('<.*?>')
            result = res1.text.split('<div class="t-ZTKy">')
            print(len(result), "=============")
            lis = []
            cl = CLASSIFIER()

            for i in range(1, len(result)):
                try:
                    review = result[i].split('</div>')[0]
                    review = re.sub(clean, " ", review).replace("\n", " ")
                    print(review, "===========================================")
                    lis.append(review)
                    cnt = cnt + len(lis)
                    print(cnt, "cnttt*******************************************")
                    from nltk.sentiment.vader import SentimentIntensityAnalyzer
                    pstv = 0
                    ngtv = 0
                    ntl = 0
                    sid = SentimentIntensityAnalyzer()
                    ss = sid.polarity_scores(review)
                    a = float(ss['pos'])
                    b = float(ss['neg'])
                    c = float(ss['neu'])
                    rating = 2.5
                    if (ss['neu'] > ss['pos'] and ss['neu'] > ss['neg']):
                        pass
                    if (ss['neg'] > ss['pos']):
                        negva = 5 - (5 * ss['neg'])
                        if negva > 2.5:
                            negva = negva - 2.5
                        rating = negva
                    else:
                        negva = 5 * ss['pos']
                        if negva < 2.5:
                            negva = negva + 2.5
                        rating = negva

                    avgrating.append(rating)

                    import re
                    cl = re.compile('\W')
                    review = re.sub(cl, " ", review)
                    # cmd.execute(
                    #     "INSERT INTO `review` VALUES(NULL,'" + str(pid) + "','" + str() + "','" + str(rating) + "')")
                    # con.commit()
                    qry3 = "Insert into crawlresult values (NULL,%s,%s,%s)"
                    val3 = (bid, review, rating)
                    iud(qry3, val3)

                except Exception as e:
                    print("looooooooooooooooooooo++++++++++++++++++++++++", e)


        return '''<script>alert('Added successfully');window.location='/addbook';</script>'''

    else:
        return '''<Script>alert("Isbn already present");window.location='/addbook';</script>'''







@app.route('/addsectioncode',methods=['post'])
@login_required
def addsectioncode():
    genre = request.form['Genre']
    location = request.form['Location']
    qry2 = "Select * from section where name=%s"
    val = (genre)
    res = selectone(qry2,val)

    if res is None:
        qry = "insert into section values (NULL,%s,%s)"
        val = (genre,location)
        iud(qry,val)
        return '''<script>alert('Added successfully');window.location='/addsection';</script>'''
    else:
        return '''<script>alert('Genre already present');window.location='/addsection';</script>'''


@app.route('/addexpensecode',methods=['post'])
@login_required
def addexpensecode():
    date = request.form['date']
    amount = request.form['amount']
    description = request.form['description']

    qry = "insert into expense values (NULL,%s,%s,%s)"
    val = (date,description,amount)
    iud(qry,val)
    return '''<script>alert('Added successfully');window.location='/addexpense';</script>'''




@app.route('/addemployeecode',methods=['post'])
@login_required
def addemployeecode():

    fname=request.form['fname']
    lname = request.form['lname']
    gender = request.form['radio-inline']
    hname = request.form['hname']
    place = request.form['place']
    post = request.form['post']
    pin = request.form['pin']
    dob = request.form['dob']
    phoneno = request.form['phoneno']
    emailid = request.form['emailid']
    jdate = request.form['jdate']
    password = request.form['password']

    qry = "select * from employee where emailid=%s"
    res = selectone(qry,emailid)

    if res is None:

        qry1 = "insert into login values (NULL,%s,%s,'Employee')"
        val1 = (emailid,password)
        lid = iud(qry1,val1)

        qry2 = "insert into employee values (NULL,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,NULL,'Present')"
        val2 = (fname,lname,gender,hname,place,post,pin,phoneno,emailid,lid,dob,jdate)
        iud(qry2,val2)
        qry = "SELECT `login`.`password` FROM `employee`  JOIN `login` ON `login`.`l_id` = `employee`.`l_id` WHERE emailid=%s"
        s = selectone(qry, emailid)
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
            msg['To'] = emailid
            msg['From'] = 'amanbookstall3@gmail.com'
            try:
                gmail.send_message(msg)
            except Exception as e:
                print("COULDN'T SEND EMAIL", str(e))
            return '''<script>alert('Added successfully');window.location='/addemployee';</script>'''

    else:
        return '''<script>alert('Employee already present');window.location='/addemployee';</script>'''


@app.route('/logout')
def logout():
    session.clear()
    return redirect('/')


app.run(debug=True)