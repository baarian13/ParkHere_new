'''
Created on Oct 27, 2016

@author: henrylevy
'''
from tornado import httpclient
import urllib

def createUser(email, password, first, last, phone, seeker, owner):
    http_client = httpclient.HTTPClient()
    url = 'http://localhost:8888/signup'
    body = urllib.urlencode({'email'    : email,
                             'password' : password,
                             'first'    : first,
                             'last'     : last,
                             'phone'    : phone,
                             'seeker'   : seeker,
                             'owner'    : owner})
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
#     createUser('user6@test.com', 'password', 'first', 'last', '123-456-7890', 1, 1)
    signIn('user6@test.com', 'password')