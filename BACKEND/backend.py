from flask import Flask
from flask import request
 
app=Flask(__name__)

@app.route('/')
def blank():
    print(__name__)
    return 'leave.'

@app.route('/post',methods=["POST"])
def post():
    payload=request.form['payload']
    return payload

if __name__=="__main__":
    app.run(host='0.0.0.0')