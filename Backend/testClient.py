'''
Created on Oct 27, 2016

@author: henrylevy
'''
from tornado import httpclient
import urllib, base64

def buildImgStr(path):
    with open(path, 'rb') as image_file:
        return base64.b64encode(image_file.read())

def createUser(email, password, first, last, phone, seeker, owner, img=None):
    http_client = httpclient.HTTPClient()
    url = 'http://localhost:8888/signup'
    body = urllib.urlencode({'email'      : email,
                             'password'   : password,
                             'first'      : first,
                             'last'       : last,
                             'phone'      : phone,
                             'seeker'     : seeker,
                             'owner'      : owner,
                             'profilePic' : img})
    req = httpclient.HTTPRequest(url, 'POST', body=body)
    
    # I have used synchronous one (you can use async one with callback)
    client = httpclient.HTTPClient()
    
    res = client.fetch(req)
    print res
    http_client.close()
    
def signIn(email, password):
    http_client = httpclient.HTTPClient()
    url = 'http://localhost:8888/signin'
    body = urllib.urlencode({'email'    : email,
                             'password' : password})
    req = httpclient.HTTPRequest(url, 'POST', body=body)
    
    # I have used synchronous one (you can use async one with callback)
    client = httpclient.HTTPClient()
    
    res = client.fetch(req)
    print res.body
    http_client.close()

if __name__ == '__main__':
    img = buildImgStr('/Users/henrylevy/Downloads/default.jpg')
    createUser('default12@test.com', 'password', 'first', 'last', '123-456-7890', 1, 1, img)
    
#     signIn('user6@test.com', 'password')