'''
Created on Oct 27, 2016

@author: henrylevy
'''
from tornado import httpclient
import urllib, base64

# ip = '35.160.111.133'
ip = 'localhost'
#ip = 'parkhere.cghr1zvgeuqd.us-west-1.rds.amazonaws.com'
def buildImgStr(path):
    with open(path, 'rb') as image_file:
        return base64.b64encode(image_file.read())

def createUser(http_client, email, password, first, last, phone, seeker, owner, img=None):
    url = 'http://{0}:8888/signup'.format(ip)
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
    return res.headers['Set-Cookie']
    
def signIn(http_client, email, password):
    url = 'http://{0}:8888/signin'.format(ip)
    body = urllib.urlencode({'email'    : email,
                             'password' : password})
    req = httpclient.HTTPRequest(url, 'POST', body=body)
    res = http_client.fetch(req)
    return res.headers['set-cookie']

def searchSpot(cookie, http_client, address):
    url = 'http://{0}:8888/search/spot'.format(ip)
    headers = {'Cookie'  : cookie}
    print cookie
    args = urllib.urlencode({'address' : address})
    url = url + '?' + args
    print url
    req = httpclient.HTTPRequest(url, 'GET', headers=headers)
    
    res = http_client.fetch(req)
    print res.body

def postSpot(cookie, http_client, address, spotType, isCovered,
             cancelationPolicy, price, start, end, recurring):
    headers = {"Cookie": cookie}
    url = 'http://{0}:8888/post/spot'.format(ip)
    body = urllib.urlencode({'address'           : address,
                             'spotType'          : spotType,
                             'isCovered'         : isCovered,
                             'cancelationPolicy' : cancelationPolicy,
                             'price'             : price,
                             'start'             : start,
                             'end'               : end,
                             'isRecurring'       : recurring})
    req = httpclient.HTTPRequest(url, 'POST', body=body, headers=headers)
    
    http_client.fetch(req)

if __name__ == '__main__':
    http_client = httpclient.HTTPClient()
    #img = buildImgStr('/Users/henrylevy/Downloads/default.jpg')
    #cookie = createUser(http_client, '', '', 'first', 'last', '123-456-7890', 1, 1)
    
    cookie = signIn(http_client, 'rob@rob.com', 'Password1$')
    postSpot(cookie, http_client, '707 West 28th street, Los Angeles CA, 90007', '0', '0', "0", '10.00', "2016-10-12 12:00:00", "2016-10-12 14:00:00", '0')
    searchSpot(cookie, http_client, '700 West 28th street, Los Angeles CA, 90007')
    http_client.close()
    