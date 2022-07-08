from flask import Flask, g
from flask import request
import openai
import keyboard
import json

#api key
key = 'api-key'

#openai api request
openai.api_key = key
openai.Answer()
def main(payload):
  global answer
  response = openai.Completion.create(
    engine="text-davinci-002",
    prompt=payload,
    temperature=0.7,
    max_tokens=64,
    top_p=1,
    frequency_penalty=0,
    presence_penalty=0
  )
  resp = str(response)
  answer = resp[resp.index("\"text\": \"")+9:resp.index("\"",resp.index("\"text\": \"")+9)]
  answer = answer.replace("\\n","")
  return answer
  


#flask server
app=Flask(__name__)

@app.route('/')
def blank():
    print(__name__)
    return 'leave.'

@app.route('/post',methods=["POST"])
def post():
    payload=request.form['payload']
    main(payload)
    return answer

if __name__=="__main__":
    app.run(host='0.0.0.0')