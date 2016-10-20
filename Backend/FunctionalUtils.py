'''
Created on Oct 17, 2016

@author: henrylevy
'''
import hashlib
import uuid
from geopy.geocoders import Nominatim

geolocator = Nominatim()

def saltPassword(password, salt):
    '''
    :type password: str
    :type salt: str
    '''
    return hashlib.sha512(password + salt).hexdigest()

def createSalt():
    return uuid.uuid4().hex

def getLatitudeLongitude(address):
    location = geolocator.geocode(address)
    return location.latitude, location.longitude 