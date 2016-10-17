'''
Created on Oct 17, 2016

@author: henrylevy
'''
import hashlib, uuid

def saltPassword(password, salt):
    '''
    :type password: str
    :type salt: str
    '''
    return hashlib.sha512(password + salt).hexdigest()

def createSalt():
    return uuid.uuid4().hex