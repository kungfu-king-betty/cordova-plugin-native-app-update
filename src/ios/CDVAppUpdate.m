//
//  CDVAppUpdate
//
//  Created by Austen Zeh <developerDawg@gmail.com> on 2020-03-16
//
#import "CDVAppUpdate.h"
#import <objc/runtime.h>
#import <Cordova/CDVViewController.h>

static NSString *const TAG = @"CDVAppUpdate";

@implementation CDVAppUpdate

-(BOOL) needsUpdate:(CDVInvokedUrlCommand*)command
{
    NSDictionary* infoDictionary = [[NSBundle mainBundle] infoDictionary];
    NSString* appID = infoDictionary[@"CFBundleIdentifier"];
    if ([command.arguments count] > 0) {
        appID = [command.arguments objectAtIndex:0];
    }
    NSURL* url = [NSURL URLWithString:[NSString stringWithFormat:@"http://itunes.apple.com/lookup?bundleId=%@", appID]];
    NSData* data = [NSData dataWithContentsOfURL:url];
    NSDictionary* lookup = [NSJSONSerialization JSONObjectWithData:data options:0 error:nil];
    BOOL update_avail = NO;

    NSLog(@"%@ Checking for app update", TAG);
    if ([lookup[@"resultCount"] integerValue] == 1) {
        NSString* appStoreVersion = lookup[@"results"][0][@"version"];
        NSString* currentVersion = infoDictionary[@"CFBundleShortVersionString"];
        if (![appStoreVersion isEqualToString:currentVersion]){
            NSLog(@"%@ Need to update [%@ != %@]", TAG, appStoreVersion, currentVersion);
            update_avail = YES;
        }
    }

    CDVPluginResult* result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsBool:update_avail];
    [result setKeepCallbackAsBool:YES];
    [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
}

@end