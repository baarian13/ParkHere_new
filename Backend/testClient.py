'''
Created on Oct 27, 2016

@author: henrylevy
'''
from tornado import httpclient
import urllib, base64

def buildImgStr(path):
    with open(path, 'rb') as image_file:
        return base64.b64encode(image_file.read())

def createUser(http_client, email, password, first, last, phone, seeker, owner, img=None):
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
    
    res = http_client.fetch(req)
    print res
    
def signIn(http_client, email, password):
    url = 'http://localhost:8888/signin'
    body = urllib.urlencode({'email'    : email,
                             'password' : password})
    req = httpclient.HTTPRequest(url, 'POST', body=body)
    res = http_client.fetch(req)
    return res.headers['set-cookie']

def searchSpot(cookie, http_client, address):
    url = 'http://localhost:8888/search/spot'
    headers = {'Cookie'  : cookie}
    args = urllib.urlencode({'address' : address})
    url = url + '?' + args
    req = httpclient.HTTPRequest(url, 'GET', headers=headers)
    
    res = http_client.fetch(req)
    print res.body

def postSpot(cookie, http_client, address, spotType, isCovered,
             cancelationPolicy, price, start, end, recurring):
    headers = {"Cookie": cookie}
    print headers
    url = 'http://localhost:8888/post/spot'
    body = urllib.urlencode({'address'           : address,
                             'spotType'          : spotType,
                             'isCovered'         : isCovered,
                             'cancelationPolicy' : cancelationPolicy,
                             'price'             : price,
                             'start'             : start,
                             'end'               : end,
                             'isRecurring'       : recurring})
    req = httpclient.HTTPRequest(url, 'POST', body=body, headers=headers)
    
    res = http_client.fetch(req)
    return res.headers['set-cookie']

if __name__ == '__main__':
    http_client = httpclient.HTTPClient()
    img = buildImgStr('/Users/henrylevy/Downloads/default.jpg')
#     createUser('default12@test.com', 'password', 'first', 'last', '123-456-7890', 1, 1, img)
    
    cookie = signIn(http_client, 'default12@test.com', 'password')
    searchSpot(cookie, http_client, '707 West 28th street, Los Angeles CA, 90007')
    http_client.close()
    