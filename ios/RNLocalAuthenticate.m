
#import "RNLocalAuthenticate.h"

#import <LocalAuthentication/LocalAuthentication.h>

@implementation RNLocalAuthenticate

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}
RCT_EXPORT_MODULE()


RCT_EXPORT_METHOD(HasHardware:(RCTResponseSenderBlock)resolve) {
    LAContext *context = [LAContext new];
    NSError *error = nil;
    
    BOOL isSupported = [context canEvaluatePolicy:LAPolicyDeviceOwnerAuthenticationWithBiometrics error:&error];
    BOOL isAvailable;
    
    if (@available(iOS 11.0, *)) {
        isAvailable = isSupported || error.code != LAErrorBiometryNotAvailable;
    } else {
        isAvailable = isSupported || error.code != LAErrorTouchIDNotAvailable;
    }
    
    resolve(@[[NSNumber numberWithLong: isAvailable]]);
}

RCT_EXPORT_METHOD(IsEnrolled:(RCTResponseSenderBlock)resolve) {
    LAContext *context = [LAContext new];
    NSError *error = nil;
    
    BOOL isSupported = [context canEvaluatePolicy:LAPolicyDeviceOwnerAuthenticationWithBiometrics error:&error];
    BOOL isEnrolled = isSupported && error == nil;
    
    resolve(@[[NSNumber numberWithLong: isEnrolled]]);
}


RCT_EXPORT_METHOD(SupportedAuthenticationTypes:(RCTResponseSenderBlock)resolve) {
    NSMutableArray *results = [NSMutableArray array];
    if ([[self class] isTouchIdDevice]) {
        [results addObject:@("TouchId")];
    }

    if ([[self class] isFaceIdDevice]) {
        [results addObject:@("FaceId")];
    }

    resolve(results);
}


RCT_EXPORT_METHOD(Authenticate:(NSString *)reason resolve:(RCTResponseSenderBlock)resolve) {
    NSString *warningMessage;
    
    if ([[self class] isFaceIdDevice]) {
        NSString *usageDescription = [[[NSBundle mainBundle] infoDictionary] objectForKey:@"NSFaceIDUsageDescription"];
        
        if (!usageDescription) {
            warningMessage = @"FaceID is available but has not been configured. To enable FaceID, provide `NSFaceIDUsageDescription`.";
        }
    }
    
    LAContext *context = [LAContext new];
    
    if (@available(iOS 11.0, *)) {
        context.interactionNotAllowed = false;
    }
    
    [context evaluatePolicy:LAPolicyDeviceOwnerAuthentication
            localizedReason:reason
                      reply:^(BOOL success, NSError *error) {
                          resolve(@{
                                    @"success": @(success),
                                    @"error": error == nil ? [NSNull null] : [self convertErrorCode:error],
                                    });
                      }];
}


- (NSString *)convertErrorCode:(NSError *)error
{
    switch (error.code) {
        case LAErrorSystemCancel:
            return @"system_cancel";
        case LAErrorAppCancel:
            return @"app_cancel";
        case LAErrorTouchIDLockout:
            return @"lockout";
        case LAErrorUserFallback:
            return @"user_fallback";
        case LAErrorUserCancel:
            return @"user_cancel";
        case LAErrorTouchIDNotAvailable:
            return @"not_available";
        case LAErrorInvalidContext:
            return @"invalid_context";
        case LAErrorTouchIDNotEnrolled:
            return @"not_enrolled";
        case LAErrorPasscodeNotSet:
            return @"passcode_not_set";
        case LAErrorAuthenticationFailed:
            return @"authentication_failed";
        default:
            return [@"unknown: " stringByAppendingFormat:@"%ld, %@", (long) error.code, error.localizedDescription];
    }
}


+ (BOOL)isFaceIdDevice
{
    static BOOL isFaceIDDevice = NO;
    
    if (@available(iOS 11.0, *)) {
        static dispatch_once_t onceToken;
        
        dispatch_once(&onceToken, ^{
            LAContext *context = [LAContext new];
            [context canEvaluatePolicy:LAPolicyDeviceOwnerAuthenticationWithBiometrics error:nil];
            isFaceIDDevice = context.biometryType == LABiometryTypeFaceID;
        });
    }
    
    return isFaceIDDevice;
}

+ (BOOL)isTouchIdDevice
{
    static BOOL isTouchIDDevice = NO;
    static dispatch_once_t onceToken;
    
    dispatch_once(&onceToken, ^{
        LAContext *context = [LAContext new];
        [context canEvaluatePolicy:LAPolicyDeviceOwnerAuthenticationWithBiometrics error:nil];
        if (@available(iOS 11.0, *)) {
            isTouchIDDevice = context.biometryType == LABiometryTypeTouchID;
        } else {
            isTouchIDDevice = true;
        }
    });
    
    return isTouchIDDevice;
}

@end
  
