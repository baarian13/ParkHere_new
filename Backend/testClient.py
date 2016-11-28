'''
Created on Oct 27, 2016

@author: henrylevy
'''
from tornado import httpclient
import urllib, base64
import unittest
import json


# ip = '35.160.111.133'
# ip = '35.163.38.167'
ip = 'localhost'
#ip = 'parkhere.cghr1zvgeuqd.us-west-1.rds.amazonaws.com'
#ip = 'ec2-35-163-38-167.us-west-2.compute.amazonaws.com'
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
    try:
        cookie = res.headers["set-cookie"]
    except:
        cookie = None
    return cookie, res.body
    
def signIn(http_client, email, password):
    url = 'http://{0}:8888/signin'.format(ip)
    body = urllib.urlencode({'email'    : email,
                             'password' : password})
    req = httpclient.HTTPRequest(url, 'POST', body=body)
    res = http_client.fetch(req)
    try:
        cookie = res.headers["set-cookie"]
    except:
        cookie = None
    return cookie, res.body

def searchSpot(cookie, http_client, address):
    url = 'http://{0}:8888/search/spot/location'.format(ip)
    headers = {'Cookie'  : cookie}
    args = urllib.urlencode({'address' : address})
    url = url + '?' + args
    req = httpclient.HTTPRequest(url, 'GET', headers=headers)
    
    res = http_client.fetch(req)
    return res.body

def searchSpotTime(cookie, http_client, start, end):
    url = 'http://{0}:8888/search/spot/date'.format(ip)
    headers = {'Cookie'  : cookie}
    args = urllib.urlencode({'start' : start, 
                             'end'   : end})
    url = url + '?' + args
    req = httpclient.HTTPRequest(url, 'GET', headers=headers)
    
    res = http_client.fetch(req)
    return res.body

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
    
    res = http_client.fetch(req)
    return res.body

def checkUser(cookie, http_client, email):
    url = 'http://{0}:8888/check/user'.format(ip)
    headers = {'Cookie'  : cookie}
    args = urllib.urlencode({'email' : email})
    url = url + '?' + args
    req = httpclient.HTTPRequest(url, 'GET', headers=headers)
    
    res = http_client.fetch(req)
    return res.body

def viewUser(cookie, http_client, email):
    url = 'http://{0}:8888/view/user'.format(ip)
    headers = {'Cookie'  : cookie}
    args = urllib.urlencode({'email' : email})
    url = url + '?' + args
    req = httpclient.HTTPRequest(url, 'GET', headers=headers)
    res = http_client.fetch(req)
    return res.body

def rateUser(cookie, http_client, email, rating):
    headers = {"Cookie": cookie}
    url = 'http://{0}:8888/rate/user'.format(ip)
    body = urllib.urlencode({'email'        : email,
                             'rating'       : rating})
    req = httpclient.HTTPRequest(url, 'POST', body=body, headers=headers)
    
    res = http_client.fetch(req)
    print "body:"
    print res.body

def rateSpot(cookie, http_client, spotId, rating):
    headers = {"Cookie": cookie}
    url = 'http://{0}:8888/rate/spot'.format(ip)
    body = urllib.urlencode({'spotId'        : spotId,
                             'rating'       : rating})
    req = httpclient.HTTPRequest(url, 'POST', body=body, headers=headers)

    res = http_client.fetch(req)
    print "body:"
    print res.body

def modifyUser1(cookie, http_client, phone, isSeeker):
    headers = {"Cookie": cookie}
    url = 'http://{0}:8888/modify/user'.format(ip)
    body = urllib.urlencode({'phone'        : phone,
                             'isSeeker'     :  isSeeker})
    req = httpclient.HTTPRequest(url, 'POST', body=body, headers=headers)
    
    res = http_client.fetch(req)
    print "body:"
    print res.body

def viewSpot(cookie, http_client, spotID):
    url = 'http://{0}:8888/view/spot'.format(ip)
    headers = {'Cookie'  : cookie}
    args = urllib.urlencode({'spotID' : spotID})
    url = url + '?' + args
    req = httpclient.HTTPRequest(url, 'GET', headers=headers)
    res = http_client.fetch(req)
    return res.body

def deleteSpot(cookie, http_client, spotID):
    headers = {"Cookie": cookie}
    url = 'http://{0}:8888/delete/spot'.format(ip)
    body = urllib.urlencode({'spotID': spotID})
    req = httpclient.HTTPRequest(url, 'POST', body=body, headers=headers)
    
    res = http_client.fetch(req)
    return res.body

def viewPostings(cookie, http_client, email):
    url = 'http://{0}:8888/view/postings'.format(ip)
    headers = {'Cookie'  : cookie}
    args = urllib.urlencode({'email' : email})
    url = url + '?' + args
    req = httpclient.HTTPRequest(url, 'GET', headers=headers)
    res = http_client.fetch(req)
    print res.body
    return res.body

def cancelReservation(cookie, http_client, spotID):
    headers = {"Cookie": cookie}
    url = 'http://{0}:8888/cancel/reservation'.format(ip)
    body = urllib.urlencode({'spotID': spotID})
    req = httpclient.HTTPRequest(url, 'POST', body=body, headers=headers)
    
    res = http_client.fetch(req)
    return res.body

def spotHistory(cookie, http_client, email):
    headers = {"Cookie": cookie}
    url = 'http://{0}:8888/view/history'.format(ip)
    args = urllib.urlencode({'email' : email})
    url = url + '?' + args
    req = httpclient.HTTPRequest(url, 'GET', headers=headers)

    res = http_client.fetch(req)
    return res.body

def contactCustomerService(cookie, http_client, email, message):
    headers = {"Cookie": cookie}
    url = 'http://{0}:8888/contact/service'.format(ip)
    body = urllib.urlencode({'email': email,
                             'message': message})
    req = httpclient.HTTPRequest(url, 'POST', body=body, headers=headers)
    
    res = http_client.fetch(req)
    return res.body
# if __name__ == '__main__':
#     http_client = httpclient.HTTPClient()
#     #img = buildImgStr('/Users/henrylevy/Downloads/default.jpg')
#     cookie = createUser(http_client, 'rob1@rob.com', 'Password1$', 'first', 'last', '123-456-7890', 1, 1)
#     cookie = signIn(http_client, 'rob@rob.com', 'Password1$')
#     checkUser(cookie, http_client, "rob1@rob.com")
#     checkUser(cookie, http_client, "rob@rob.com")
     # postSpot(cookie, http_client, '707 West 28th street, Los Angeles CA, 90007', '0', '0', "0", '10.00', "2016-10-12 12:00:00", "2016-10-12 14:00:00", '0')
#     searchSpot(cookie, http_client, '700 West 28th street, Los Angeles CA, 90007')
#     http_client.close()


class TestParkHereMethods(unittest.TestCase):

    # def test_signup(self):
    #     http_client = httpclient.HTTPClient()
    #     cookie, code = createUser(http_client, '', 'Password1$', 'first', 'last', '123-456-7890', 1, 1)
    #     # cookie, code = createUser(http_client, 'rob6@rob.com', 'Password1$', 'first', 'last', '123-456-7890', 1, 1)
    #     # self.assertNotEqual(code, '401')
    #     # info = viewUser(cookie, http_client, 'rob6@rob.com')
    #     # jsondata = json.loads(info)
    #     # self.assertEqual(jsondata["isSeeker"], 1)
    #     # cookie, code = createUser(http_client, 'rob@rob.com', 'Password1$', 'first', 'last', '123-456-7890', 1, 1)
    #     # self.assertEqual(code, '401')
    #     http_client.close()


    # def test_signin(self):
    #     http_client = httpclient.HTTPClient()
    #     cookie, code = signIn(http_client, 'rob@rob.com', 'Password1')
    #     self.assertEqual(code, '401')
    #     cookie, code = signIn(http_client, 'ob@rob.com', 'Password1')
    #     self.assertEqual(code, '401')
    #     cookie, code = signIn(http_client, 'rob@rob.com', 'Password1$')
    #     self.assertEqual(code, '200')
    #     http_client.close()




    # def test_check_user(self):
    #     http_client = httpclient.HTTPClient()
    #     cookie, code = signIn(http_client, 'rob@rob.com', 'Password1$')
    #     res = checkUser(cookie, http_client, "rob@rob.com")
    #     self.assertEqual(int(res), 1)
    #     res = checkUser(cookie, http_client, "rob1@rob.com")
    #     self.assertEqual(int(res), 0)
    #     res = checkUser(cookie, http_client, "rob45@rob.com")
    #     self.assertEqual(int(res), 0)
    #     http_client.close()
    #     # self.assertEqual(s.split(), ['hello', 'world'])
    #     # # check that s.split fails when the separator is not a string
    #     # with self.assertRaises(TypeError):
    #     #     s.split(2)

    # def test_view_user(self):
    #     http_client = httpclient.HTTPClient()
    #     cookie, code = signIn(http_client, 'rob@rob.com', 'Password1$')

    #     info = viewUser(cookie, http_client, "rob@rob.com")
    #     jsondata = json.loads(info)
    #     self.assertEqual(jsondata["rating"], 0.0)
    #     self.assertEqual(jsondata["last"], 'last')
    #     self.assertEqual(jsondata["first"], 'first')
    #     self.assertEqual(jsondata["isSeeker"], 1)
    #     self.assertEqual(jsondata["isOwner"], 1)
    #     self.assertEqual(jsondata["phoneNumber"], '123-456-7890')
    #     self.assertEqual(jsondata["email"], 'rob@rob.com')
    #     http_client.close()

    # def test_rate_user(self):
    #     http_client = httpclient.HTTPClient()
    #     # cookie, code = createUser(http_client, 'rate@user.com', 'Password1$', 'first', 'last', '123-456-7890', 1, 1)
    #     cookie, code = signIn(http_client, 'rob@rob.com', 'Password1$')
    #     info = viewUser(cookie, http_client, 'rate@user.com')
    #     jsondata = json.loads(info)
    #     print jsondata["rating"]
    #     self.assertEqual(jsondata["rating"], 0.0)
    #     rateUser(cookie, http_client,'rate@user.com', 5)
    #     info = viewUser(cookie, http_client, 'rate@user.com')
    #     jsondata = json.loads(info)
    #     print jsondata["rating"]
    #     self.assertEqual(jsondata["rating"], 5.0)
    #       http_client.close()

    # def test_rate_spot(self):
    #      http_client = httpclient.HTTPClient()
    #      spot = 10
    #      cookie, code = signIn(http_client, 'a@b.com', 'password1!')
    #      info = viewSpot(cookie, http_client, spot)
    #      jsondata = json.loads(info)
    #      print jsondata["rating"]
    #      self.assertEqual(jsondata["rating"], 0.0)
    #      rateSpot(cookie, http_client, spot, 5)
    #      info = viewSpot(cookie, http_client, spot)
    #      jsondata = json.loads(info)
    #      print jsondata["rating"]
    #      self.assertEqual(jsondata["rating"], 5.0)
    #      http_client.close()


    # def test_modify_user(self):
    #     http_client = httpclient.HTTPClient()
    #     cookie, code = signIn(http_client, 'rob@rob.com', 'Password1$')
    #     info = viewUser(cookie, http_client, 'rob@rob.com')
    #     jsondata = json.loads(info)
    #     self.assertEqual(jsondata["phoneNumber"], '123-456-1111')
    #     self.assertEqual(jsondata["isSeeker"], 1)
    #     modifyUser1(cookie, http_client, '123-456-1121', 0)
    #     info = viewUser(cookie, http_client, 'rob@rob.com')
    #     jsondata = json.loads(info)
    #     self.assertEqual(jsondata["phoneNumber"], '123-456-1121')
    #     self.assertEqual(jsondata["isSeeker"], 0)
    #     http_client.close()

    # def test_post_spot(self):
    #     http_client = httpclient.HTTPClient()
    #     cookie, code = signIn(http_client, 'rob@rob.com', 'Password1$')
    #     res = postSpot(cookie, http_client, '2801 Menlo Ave, Los Angeles CA, 90007', '0', '0', "0", '10.00', "2016-11-12 12:00:00", "2016-11-12 14:00:00", '0')
    #     self.assertEqual(res, '200')
    #     http_client.close()

    # def test_search_spot(self):
    #     http_client = httpclient.HTTPClient()
    #     cookie, code = signIn(http_client, 'rob@rob.com', 'Password1$')
    #     res = searchSpot(cookie, http_client,"2801 Menlo Ave, Los Angeles, CA")
    #     jsondata = json.loads(res)
    #     self.assertEqual(jsondata[0]["address"], '2801 Menlo Ave, Los Angeles CA, 90007')
    #     self.assertEqual(jsondata[0]["start"], "2016-11-12 12:00:00")
    #     self.assertEqual(jsondata[0]["end"], "2016-11-12 14:00:00")
    #     http_client.close()

    # def test_search_time_spot(self):
    #     http_client = httpclient.HTTPClient()
    #     cookie, code = signIn(http_client, 'rob2@rob.com', 'Password1$')
    #     res = searchSpotTime(cookie, http_client,"2016-10-19 11:58:00","2016-10-26 22:40:00")
    #     print res
    #     jsondata = json.loads(res)
    #     self.assertEqual(jsondata[0]["address"], '1200 West 35th street, Los Angeles CA, 90007')
    #     http_client.close()


    # def test_view_spot(self):
    #     http_client = httpclient.HTTPClient()
    #     cookie, code = signIn(http_client, 'rob@rob.com', 'Password1$')
    #     # res = postSpot(cookie, http_client, '706 West 28th street, Los Angeles CA, 90007', '0', '0', '0', '10.00', "2016-10-12 12:00:00", "2016-10-12 14:00:00", '0')
    #     res = searchSpot(cookie, http_client, '706 West 28th street, Los Angeles CA, 90007')
    #     jsondata = json.loads(res)
    #     spotID = jsondata[0]['id']
    #     res = viewSpot(cookie, http_client,spotID)
    #     jsondata = json.loads(res)
    #     self.assertEqual(jsondata["address"], '706 West 28th street, Los Angeles CA, 90007')
    #     self.assertEqual(jsondata["spotType"], 0)
    #     self.assertEqual(jsondata["isCovered"], 0)
    #     self.assertEqual(jsondata["cancelationPolicy"], 0)
    #     self.assertEqual(jsondata["price"], 10.00)
    #     self.assertEqual(jsondata["start"], u'2016-10-12 12:00:00')
    #     self.assertEqual(jsondata["end"], u'2016-10-12 14:00:00')
    #     self.assertEqual(jsondata["isRecurring"],0)
    #     http_client.close()

    # def test_delete_spot(self):
    #     http_client = httpclient.HTTPClient()
    #     cookie, code = signIn(http_client, 'rob@rob.com', 'Password1$')
    #     res = postSpot(cookie, http_client, '2800 Menlo Ave, Los Angeles CA, 90007', '0', '0', "0", '10.00', "2016-11-12 12:00:00", "2016-11-12 14:00:00", '0')
    #     res = searchSpot(cookie, http_client, '2800 Menlo Ave, Los Angeles CA, 90007')
    #     jsondata = json.loads(res)
    #     spotID = jsondata[0]['id']
    #     res = deleteSpot(cookie, http_client, spotID)
    #     self.assertEqual(res, '200')
    #     http_client.close()

    # def test_view_postings(self):
    #     http_client = httpclient.HTTPClient()
    #     cookie, code = signIn(http_client, 'rob@rob.com', 'Password1$')
    #     res = viewPostings(cookie, http_client, 'rob@rob.com')
    #     jsondata = json.loads(res)
    #     self.assertEqual(jsondata[0][0],6)
    #     self.assertEqual(jsondata[1][0],7)

    #     http_client.close()

    #def test_cancel_spot(self):
    #    http_client = httpclient.HTTPClient()
    #    cookie, code = signIn(http_client, 'qwerty@a.com', 'Password!1')
    #    spotID = 10
    #    res = cancelReservation(cookie, http_client, spotID)
    #    self.assertEqual(res, '200')
    #    http_client.close()

    #ID only
    #def test_spot_history(self):
    #    http_client = httpclient.HTTPClient()
    #    cookie, code = signIn(http_client, 'qwerty@a.com', 'Password!1')
    #    res = spotHistory(cookie, http_client,'qwerty@a.com')
    #    jsondata = json.loads(res)
    #    #self.assertEqual(jsondata[0][0], 10)
    #    self.assertEqual(len(jsondata), 2)
    #    http_client.close()
    #more data
    def test_spot_history(self):
        http_client = httpclient.HTTPClient()
        cookie, code = signIn(http_client, 'qwerty@a.com', 'Password!1')
        res = spotHistory(cookie, http_client,'qwerty@a.com')
        jsondata = json.loads(res)
        self.assertEqual(jsondata[0][1], "702 West 28th street, Los Angeles CA, 90007")
        self.assertEqual(jsondata[0][0], 10)
        http_client.close()

    #def test_contact_service(self):
    #    http_client = httpclient.HTTPClient()
    #    cookie, code = signIn(http_client, 'qwerty@a.com', 'Password!1')
    #    res = contactCustomerService(cookie, http_client, "qwerty@.com", "testing")
    #    self.assertEqual(res,'200')
    #    http_client.close()



if __name__ == '__main__':
    unittest.main()
    