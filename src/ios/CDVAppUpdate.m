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
    NSString* force_api = nil;
    NSString* force_key = nil;
    if ([command.arguments count] > 0) {
        force_api = [command.arguments objectAtIndex:0];
        force_key = [command.arguments objectAtIndex:1];
    }
    NSURL* url = [NSURL URLWithString:[NSString stringWithFormat:@"http://itunes.apple.com/lookup?bundleId=%@", appID]];
    NSData* data = [NSData dataWithContentsOfURL:url];
    NSDictionary* lookup = [NSJSONSerialization JSONObjectWithData:data options:0 error:nil];
    NSMutableDictionary *resultObj = [[NSMutableDictionary alloc]initWithCapacity:10];
    BOOL update_avail = NO;
    BOOL update_force = NO;

    NSLog(@"%@ Checking for app update", TAG);
    if ([lookup[@"resultCount"] integerValue] == 1) {
        NSString* appStoreVersion = lookup[@"results"][0][@"version"];
        NSArray* appStoreVersionArr = [appStoreVersion componentsSeparatedByString:@"."];
        NSString* currentVersion = infoDictionary[@"CFBundleShortVersionString"];
        NSArray* currentVersionArr = [currentVersion componentsSeparatedByString:@"."];

        for (int idx=0; idx<[appStoreVersionArr count]; idx++) {
            NSNumberFormatter *f = [[NSNumberFormatter alloc] init];
            f.numberStyle = NSNumberFormatterDecimalStyle;
            NSNumber* appStoreVersionNumber = [f numberFromString:[appStoreVersionArr objectAtIndex:idx]];
            NSNumber* currentVersionNumber = [f numberFromString:[currentVersionArr objectAtIndex:idx]];

            if ([currentVersionNumber compare:appStoreVersionNumber] == NSOrderedAscending) {
                NSLog(@"%@ Need to update [%@ != %@]", TAG, appStoreVersion, currentVersion);
                if ([force_api length] > 0) {
                    NSURL* force_url = [NSURL URLWithString:[NSString stringWithFormat:force_api]];
                    NSData* force_data = [NSData dataWithContentsOfURL:force_url];
                    NSDictionary* force_lookup = [NSJSONSerialization JSONObjectWithData:force_data options:0 error:nil];
                    update_force = [force_lookup objectForKey:force_key];
                    for (id key in force_lookup) {
                        [resultObj setObject:[force_lookup objectForKey:key] forKey:key];
                    }
                }
                NSLog(@"%@ Force Update: %i", TAG, update_force);
                update_avail = YES;
                break;
            }
        }
    }

    
    [resultObj setObject:[NSNumber numberWithBool:update_avail] forKey:@"update_available"];

    CDVPluginResult* result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:resultObj];
    [result setKeepCallbackAsBool:YES];
    [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
}

@end