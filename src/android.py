import os
from flask import *
from werkzeug.utils import secure_filename

from src.dbconnection import *
import smtplib
from email.mime.text import MIMEText
from flask_mail import Mail


app = Flask(__name__)
mail=Mail(app)
app.config['MAIL_SERVER']='smtp.gmail.com'
app.config['MAIL_PORT'] = 587
app.config['MAIL_USERNAME'] = 'amanbooksatll3@gmail.com'
app.config['MAIL_PASSWORD'] = '@manbook3'
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
def registration():
    name = request.form['name']
    place = request.form['place']
    post = request.form['post']
    pin = request.form['pin']
    emailid = request.form['emailid']
    phoneno = request.form['phoneno']
    username = request.form['uname']
    password = request.form['pass']

    qry1 = "insert into login values (NULL,%s,%s,'Publisher')"
    val1 = (username, password)
    lid = iud(qry1, val1)

    qry = "insert into publisher values(NULL,%s,%s,%s,%s,%s,%s,%s)"
    val = (name,place,post,pin,emailid,phoneno,lid)
    iud(qry,val)

    return jsonify({'task':'success'})


@app.route('/registrationcode2',methods=['post'])
def registration2():
    fname = request.form['fname']
    lname = request.form['lname']
    place = request.form['place']
    post = request.form['post']
    pin = request.form['pin']
    emailid = request.form['emailid']
    phoneno = request.form['phoneno']
    username = request.form['uname']
    password = request.form['pass']

    qry1 = "insert into login values (NULL,%s,%s,'Customer')"
    val1 = (username, password)
    lid = iud(qry1, val1)

    qry = "insert into customer values(NULL,%s,%s,%s,%s,%s,%s,%s,%s)"
    val = (fname,lname,place,post,pin,phoneno,emailid,lid)
    iud(qry,val)

    return jsonify({'task':'success'})

@app.route('/addbook',methods=['post'])
def addbook():
    title = request.form['title']
    author = request.form['author']
    publishdate = request.form['publishdate']
    genre = request.form['genre']
    pageno = request.form['pageno']
    price = request.form['price']
    description = request.form['description']
    pic1 = request.files['file']
    pic1name=secure_filename(pic1.filename)
    pic1.save(os.path.join('static/homepage',pic1name))

    pic2 = request.files['file2']
    pic2name = secure_filename(pic2.filename)
    pic2.save(os.path.join('static/bookpic', pic2name))
    lid = request.form['lid']
    qry = "insert into p_book values(NULL,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s)"
    val = (title,author,publishdate,genre,pageno,description,price,pic1name,pic2name,lid)
    iud(qry,val)

    return jsonify({'task':'success'})



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
                gmail.login('amanbooksatll3@gmail.com', '@manbook3')
            except Exception as e:
                print("Couldn't setup email!!" + str(e))
            msg = MIMEText("Your new password id : " + str(s[0]))
            print(msg)
            msg['Subject'] = 'Forensic Password'
            msg['To'] = email
            msg['From'] = 'amanbooksatll3@gmail.com'
            try:
                gmail.send_message(msg)
            except Exception as e:
                print("COULDN'T SEND EMAIL", str(e))
            return '''<script>alert("SEND"); window.location="/"</script>'''
    except:
        return '''<script>alert("PLEASE ENTER VALID DETAILS"); window.location="/"</script>'''


app.run(host="0.0.0.0",port=5000)