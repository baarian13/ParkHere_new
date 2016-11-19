'''
Created on Dec 21, 2015

@author: henrylevy
'''

import base64
# from boto.s3.connection import S3Connection
# from boto.s3.key import Key
import boto

class ObjectStorageManager(object):
    
    def __init__(self, bucket, user=None, password=None):
        '''
        :type bucket: str
        :type user: str
        :type password: str 
        '''
        
        self.bucketName = bucket
        self.userName = user
        self.password = password
        self.connect()

    @property
    def bucket(self):
        if not self._bucket:
            self._bucket = self.connection.lookup(self.bucketName)
        return self._bucket
    
    @property
    def connection(self):
        if not self._connection:
            self._connection = boto.connect_s3('AKIAIMALNLZEYG773ZZA','1mUPxttA0l1fhVez9S2SIfXTwVKo+1/d1zcjODPw')
        return self._connection

    def connect(self, bucketName=None):
        '''
        :type bucketName: str
        '''
        
        self._connection = boto.connect_s3('AKIAIMALNLZEYG773ZZA','1mUPxttA0l1fhVez9S2SIfXTwVKo+1/d1zcjODPw')
        self._bucket = self.connection.lookup(bucketName or self.bucketName)

    def uploadMedia(self, path, contentAsString):
        '''
        :type path: str
        :type contentAsString: str
        '''
        
        # k = Key(self.bucket)
        # k.key = path
        k = self.bucket.new_key(path)
        # while len(contentAsString) % 4 != 0:
        #     contentAsString += "="
        print contentAsString
        k.set_contents_from_string(base64.urlsafe_b64decode(contentAsString.encode("ascii")))
        k.set_metadata('Content-Type', 'image/jpeg')
        
        # may need to make private read
        k.set_acl('public-read')
        
    def deleteKeys(self, paths):
        '''
        :type paths: [str]
        '''
        
        self.bucket.delete_keys(paths)
        
    def downloadPictureAsString(self, path):
        '''
        :type paths: str
        '''
        
        k = self.bucket.get_key(path)
        if k:
            return k.get_contents_as_string()
        else:
            raise IOError("could not find picture")
    
    def downloadPictureAsB64(self, path):
        '''
        :type paths: str
        '''
        return base64.b64encode(self.download_picture_as_string(path))
