//
//  Created by Austen Zeh <developerDawg@gmail.com>
//

#import <Cordova/CDVPlugin.h>
#import "AppDelegate.h"

@interface AppUpdate : CDVPlugin {}

- (BOOL) needsUpdate:(CDVInvokedUrlCommand*)command;

@end

