import pymysql


def iud(qry,val):
    con=pymysql.connect(host='localhost',port=3306,user='root',password='',db='books')
    cmd=con.cursor()
    cmd.execute(qry,val)
    id=cmd.lastrowid
    con.commit()
    con.close()
    return id

def selectone(qry,val):
    con=pymysql.connect(host='localhost',port=3306,user='root',password='',db='books')
    cmd=con.cursor()
    cmd.execute(qry,val)
    res=cmd.fetchone()
    return res

def selectone2(qry):
    con=pymysql.connect(host='localhost',port=3306,user='root',password='',db='books')
    cmd=con.cursor()
    cmd.execute(qry)
    res=cmd.fetchone()
    return res


def selectall(qry):
    con=pymysql.connect(host='localhost',port=3306,user='root',password='',db='books')
    cmd=con.cursor()
    cmd.execute(qry)
    res=cmd.fetchall()
    return res
def selectall2(qry,val):
    con=pymysql.connect(host='localhost',port=3306,user='root',password='',db='books')
    cmd=con.cursor()
    cmd.execute(qry,val)
    res=cmd.fetchall()
    return res