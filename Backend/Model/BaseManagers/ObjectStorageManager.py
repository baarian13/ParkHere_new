'''
Created on Dec 21, 2015

@author: henrylevy
'''

import base64
from datetime import datetime
from boto.s3.connection import S3Connection
from boto.s3.key import Key

class ObjectStorageManager(object):
    
    def __init__(self, bucket, user, password):
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
            self._bucket = self.connection.get_bucket(self.bucketName)
        return self._bucket
    
#     def bucket(self, bucketName):
#         return self.connection.get_bucket(bucketName)
    
    @property
    def connection(self):
        if not self._connection:
            self._connection = S3Connection(self.userName, self.password)
        return self._connection

    def connect(self, bucketName=None):
        '''
        :type bucketName: str
        '''
        
        self._connection = S3Connection(self.userName, self.password)
        self._bucket = self.connection.get_bucket(bucketName or self.bucketName)

    def uploadMedia(self, path, contentAsString):
        '''
        :type path: str
        :type contentAsString: str
        '''
        
        k = Key(self.bucket)
        k.key = path
        k.set_contents_from_string(base64.b64decode(contentAsString))
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
