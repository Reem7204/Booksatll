from flask import *
from src.dbconnection import *
app = Flask(__name__)
app.secret_key="sdsd"


@app.route('/')
def first():
    return render_template("index.html")


@app.route('/login')
def login():
    return render_template("login.html")



@app.route('/logincode',methods=['post'])
def logincode():
    uname = request.form['username']
    pswd = request.form['password']
    qry = "select * from login where username = %s and password = %s"
    val = (uname,pswd)
    s = selectone(qry,val)

    if s[3]=="Admin":
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




@app.route('/deletesection')
def deletesection():
    sid=request.args.get('id')

    qry="delete from section where s_id=%s"
    val=(sid)
    iud(qry,val)

    return '''<script>alert('Deleted successfully');window.location='/viewsection';</script>'''


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


@app.route('/viewsection')
def viewsection():
    qry="SELECT * FROM `section`"
    res=selectall(qry)
    return  render_template("viewsection.html",val=res)

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

    qry1 = "insert into login values (NULL,%s,%s,'Employee')"
    val1 = (uname,password)
    lid = iud(qry1,val1)

    qry2 = "insert into employee values (NULL,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s)"
    val2 = (fname,lname,gender,hname,place,post,pin,phoneno,emailid,lid)
    iud(qry2,val2)
    return '''<script>alert('Added successfully');window.location='/addemployee';</script>'''


app.run(debug=True)