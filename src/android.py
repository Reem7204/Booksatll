from flask import *
from src.dbconnection import *
app = Flask(__name__)



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


app.run(host="0.0.0.0",port=5000)