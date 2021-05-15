//
//  Created by Austen Zeh <developerDawg@gmail.com>
//

#import <Cordova/CDVPlugin.h>
#import "AppDelegate.h"

@interface CDVAppUpdate : CDVPlugin

- (void) needsUpdate:(CDVInvokedUrlCommand*)command;

@end

