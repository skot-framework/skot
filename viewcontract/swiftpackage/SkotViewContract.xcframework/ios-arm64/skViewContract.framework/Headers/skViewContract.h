#import <Foundation/NSArray.h>
#import <Foundation/NSDictionary.h>
#import <Foundation/NSError.h>
#import <Foundation/NSObject.h>
#import <Foundation/NSSet.h>
#import <Foundation/NSString.h>
#import <Foundation/NSValue.h>

@class SVCSKUri, SVCKotlinUnit, SVCStyle, SVCSKListVCItem, SVCSKStackVCState, SVCSKWebViewVCConfig, SVCSKWebViewVCBackRequest, SVCSKWebViewVCOpenUrl, SVCSKWebViewVCRedirectParam, SVCKotlinRegex, SVCSKAlertVCShown, SVCSKAlertVCButton, SVCSKBottomSheetVCShown, SVCSKSnackBarVCShown, SVCSKSnackBarVCAction, SVCSKWindowPopupVCShown, SVCSKWindowPopupVCBehavior, SVCSKWindowPopupVCNotCancelable, SVCSKComboVCChoice, SVCIcon, SVCSKInputVCType, SVCSKInputVCTypeEMail, SVCSKInputVCTypeLongText, SVCSKInputVCTypeNormal, SVCSKInputVCTypeNumber, SVCSKInputVCTypeNumberPassword, SVCSKInputVCTypePhone, SVCKotlinThrowable, SVCKotlinArray<T>, SVCKotlinException, SVCKotlinRuntimeException, SVCKotlinIllegalStateException, SVCKotlinRegexOption, SVCKotlinRegexCompanion, SVCKotlinEnumCompanion, SVCKotlinEnum<E>, SVCKotlinMatchResultDestructured, SVCKotlinIntRange, SVCKotlinMatchGroup, SVCKotlinIntProgressionCompanion, SVCKotlinIntIterator, SVCKotlinIntProgression, SVCKotlinIntRangeCompanion;

@protocol SVCKotlinSuspendFunction0, SVCSKComponentVC, SVCSKScreenVC, SVCSKPagerVC, SVCSKTransition, SVCSKComboVC, SVCSKInputVC, SVCSKAlertVC, SVCSKBottomSheetVC, SVCSKButtonVC, SVCSKFrameVC, SVCSKImageButtonVC, SVCSKSimpleInputVC, SVCSKInputWithSuggestionsVC, SVCSKLoaderVC, SVCSKPagerWithTabsVC, SVCSKStackVC, SVCSKBoxVC, SVCSKListVC, SVCSKSnackBarVC, SVCSKWebViewVC, SVCSKWindowPopupVC, SVCKotlinFunction, SVCKotlinMatchResult, SVCKotlinSequence, SVCKotlinIterator, SVCKotlinComparable, SVCKotlinMatchGroupCollection, SVCKotlinIterable, SVCKotlinCollection, SVCKotlinClosedRange;

NS_ASSUME_NONNULL_BEGIN
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wunknown-warning-option"
#pragma clang diagnostic ignored "-Wincompatible-property-type"
#pragma clang diagnostic ignored "-Wnullability"

#pragma push_macro("_Nullable_result")
#if !__has_feature(nullability_nullable_result)
#undef _Nullable_result
#define _Nullable_result _Nullable
#endif

__attribute__((swift_name("KotlinBase")))
@interface SVCBase : NSObject
- (instancetype)init __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
+ (void)initialize __attribute__((objc_requires_super));
@end;

@interface SVCBase (SVCBaseCopying) <NSCopying>
@end;

__attribute__((swift_name("KotlinMutableSet")))
@interface SVCMutableSet<ObjectType> : NSMutableSet<ObjectType>
@end;

__attribute__((swift_name("KotlinMutableDictionary")))
@interface SVCMutableDictionary<KeyType, ObjectType> : NSMutableDictionary<KeyType, ObjectType>
@end;

@interface NSError (NSErrorSVCKotlinException)
@property (readonly) id _Nullable kotlinException;
@end;

__attribute__((swift_name("KotlinNumber")))
@interface SVCNumber : NSNumber
- (instancetype)initWithChar:(char)value __attribute__((unavailable));
- (instancetype)initWithUnsignedChar:(unsigned char)value __attribute__((unavailable));
- (instancetype)initWithShort:(short)value __attribute__((unavailable));
- (instancetype)initWithUnsignedShort:(unsigned short)value __attribute__((unavailable));
- (instancetype)initWithInt:(int)value __attribute__((unavailable));
- (instancetype)initWithUnsignedInt:(unsigned int)value __attribute__((unavailable));
- (instancetype)initWithLong:(long)value __attribute__((unavailable));
- (instancetype)initWithUnsignedLong:(unsigned long)value __attribute__((unavailable));
- (instancetype)initWithLongLong:(long long)value __attribute__((unavailable));
- (instancetype)initWithUnsignedLongLong:(unsigned long long)value __attribute__((unavailable));
- (instancetype)initWithFloat:(float)value __attribute__((unavailable));
- (instancetype)initWithDouble:(double)value __attribute__((unavailable));
- (instancetype)initWithBool:(BOOL)value __attribute__((unavailable));
- (instancetype)initWithInteger:(NSInteger)value __attribute__((unavailable));
- (instancetype)initWithUnsignedInteger:(NSUInteger)value __attribute__((unavailable));
+ (instancetype)numberWithChar:(char)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedChar:(unsigned char)value __attribute__((unavailable));
+ (instancetype)numberWithShort:(short)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedShort:(unsigned short)value __attribute__((unavailable));
+ (instancetype)numberWithInt:(int)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedInt:(unsigned int)value __attribute__((unavailable));
+ (instancetype)numberWithLong:(long)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedLong:(unsigned long)value __attribute__((unavailable));
+ (instancetype)numberWithLongLong:(long long)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedLongLong:(unsigned long long)value __attribute__((unavailable));
+ (instancetype)numberWithFloat:(float)value __attribute__((unavailable));
+ (instancetype)numberWithDouble:(double)value __attribute__((unavailable));
+ (instancetype)numberWithBool:(BOOL)value __attribute__((unavailable));
+ (instancetype)numberWithInteger:(NSInteger)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedInteger:(NSUInteger)value __attribute__((unavailable));
@end;

__attribute__((swift_name("KotlinByte")))
@interface SVCByte : SVCNumber
- (instancetype)initWithChar:(char)value;
+ (instancetype)numberWithChar:(char)value;
@end;

__attribute__((swift_name("KotlinUByte")))
@interface SVCUByte : SVCNumber
- (instancetype)initWithUnsignedChar:(unsigned char)value;
+ (instancetype)numberWithUnsignedChar:(unsigned char)value;
@end;

__attribute__((swift_name("KotlinShort")))
@interface SVCShort : SVCNumber
- (instancetype)initWithShort:(short)value;
+ (instancetype)numberWithShort:(short)value;
@end;

__attribute__((swift_name("KotlinUShort")))
@interface SVCUShort : SVCNumber
- (instancetype)initWithUnsignedShort:(unsigned short)value;
+ (instancetype)numberWithUnsignedShort:(unsigned short)value;
@end;

__attribute__((swift_name("KotlinInt")))
@interface SVCInt : SVCNumber
- (instancetype)initWithInt:(int)value;
+ (instancetype)numberWithInt:(int)value;
@end;

__attribute__((swift_name("KotlinUInt")))
@interface SVCUInt : SVCNumber
- (instancetype)initWithUnsignedInt:(unsigned int)value;
+ (instancetype)numberWithUnsignedInt:(unsigned int)value;
@end;

__attribute__((swift_name("KotlinLong")))
@interface SVCLong : SVCNumber
- (instancetype)initWithLongLong:(long long)value;
+ (instancetype)numberWithLongLong:(long long)value;
@end;

__attribute__((swift_name("KotlinULong")))
@interface SVCULong : SVCNumber
- (instancetype)initWithUnsignedLongLong:(unsigned long long)value;
+ (instancetype)numberWithUnsignedLongLong:(unsigned long long)value;
@end;

__attribute__((swift_name("KotlinFloat")))
@interface SVCFloat : SVCNumber
- (instancetype)initWithFloat:(float)value;
+ (instancetype)numberWithFloat:(float)value;
@end;

__attribute__((swift_name("KotlinDouble")))
@interface SVCDouble : SVCNumber
- (instancetype)initWithDouble:(double)value;
+ (instancetype)numberWithDouble:(double)value;
@end;

__attribute__((swift_name("KotlinBoolean")))
@interface SVCBoolean : SVCNumber
- (instancetype)initWithBool:(BOOL)value;
+ (instancetype)numberWithBool:(BOOL)value;
@end;

__attribute__((swift_name("SKFeatureInitializer")))
@interface SVCSKFeatureInitializer : SVCBase
- (instancetype)initWithInitialize:(id<SVCKotlinSuspendFunction0>)initialize onDeepLink:(void (^ _Nullable)(SVCSKUri *))onDeepLink start:(id<SVCKotlinSuspendFunction0>)start __attribute__((swift_name("init(initialize:onDeepLink:start:)"))) __attribute__((objc_designated_initializer));

/**
 @note This method converts instances of CancellationException to errors.
 Other uncaught Kotlin exceptions are fatal.
*/
- (void)initializeIfNeededUri:(SVCSKUri * _Nullable)uri completionHandler:(void (^)(SVCKotlinUnit * _Nullable, NSError * _Nullable))completionHandler __attribute__((swift_name("initializeIfNeeded(uri:completionHandler:)")));
@property (readonly) id<SVCKotlinSuspendFunction0> initialize __attribute__((swift_name("initialize")));
@property (readonly) void (^ _Nullable onDeepLink)(SVCSKUri *) __attribute__((swift_name("onDeepLink")));
@property (readonly) id<SVCKotlinSuspendFunction0> start __attribute__((swift_name("start")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SKUri")))
@interface SVCSKUri : SVCBase
- (instancetype)initWithScheme:(NSString * _Nullable)scheme host:(NSString * _Nullable)host pathSegments:(NSArray<NSString *> *)pathSegments parameters:(NSDictionary<NSString *, NSArray<NSString *> *> *)parameters __attribute__((swift_name("init(scheme:host:pathSegments:parameters:)"))) __attribute__((objc_designated_initializer));
- (NSString * _Nullable)component1 __attribute__((swift_name("component1()")));
- (NSString * _Nullable)component2 __attribute__((swift_name("component2()")));
- (NSArray<NSString *> *)component3 __attribute__((swift_name("component3()")));
- (NSDictionary<NSString *, NSArray<NSString *> *> *)component4 __attribute__((swift_name("component4()")));
- (SVCSKUri *)doCopyScheme:(NSString * _Nullable)scheme host:(NSString * _Nullable)host pathSegments:(NSArray<NSString *> *)pathSegments parameters:(NSDictionary<NSString *, NSArray<NSString *> *> *)parameters __attribute__((swift_name("doCopy(scheme:host:pathSegments:parameters:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString * _Nullable host __attribute__((swift_name("host")));
@property (readonly) NSDictionary<NSString *, NSArray<NSString *> *> *parameters __attribute__((swift_name("parameters")));
@property (readonly) NSArray<NSString *> *pathSegments __attribute__((swift_name("pathSegments")));
@property (readonly) NSString * _Nullable scheme __attribute__((swift_name("scheme")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Color")))
@interface SVCColor : SVCBase
- (instancetype)initWithName:(NSString *)name __attribute__((swift_name("init(name:)"))) __attribute__((objc_designated_initializer));
@property (readonly) NSString *name __attribute__((swift_name("name")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Icon")))
@interface SVCIcon : SVCBase
- (instancetype)initWithName:(NSString *)name __attribute__((swift_name("init(name:)"))) __attribute__((objc_designated_initializer));
@property (readonly) NSString *name __attribute__((swift_name("name")));
@end;

__attribute__((swift_name("SKTransition")))
@protocol SVCSKTransition
@required
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Style")))
@interface SVCStyle : SVCBase
- (instancetype)initWithName:(NSString *)name __attribute__((swift_name("init(name:)"))) __attribute__((objc_designated_initializer));
@property (readonly) NSString *name __attribute__((swift_name("name")));
@end;

__attribute__((swift_name("SKComponentVC")))
@protocol SVCSKComponentVC
@required
- (void)closeKeyboard __attribute__((swift_name("closeKeyboard()")));
- (void)displayErrorMessageMessage:(NSString *)message __attribute__((swift_name("displayErrorMessage(message:)")));
- (void)onRemove __attribute__((swift_name("onRemove()")));
@property SVCStyle * _Nullable style __attribute__((swift_name("style")));
@end;

__attribute__((swift_name("SKBoxVC")))
@protocol SVCSKBoxVC <SVCSKComponentVC>
@required
@property SVCBoolean * _Nullable hidden __attribute__((swift_name("hidden")));
@property NSArray<id<SVCSKComponentVC>> *items __attribute__((swift_name("items")));
@end;

__attribute__((swift_name("SKFrameVC")))
@protocol SVCSKFrameVC <SVCSKComponentVC>
@required
@property id<SVCSKScreenVC> _Nullable screen __attribute__((swift_name("screen")));
@property (readonly) NSSet<id<SVCSKScreenVC>> *screens __attribute__((swift_name("screens")));
@end;

__attribute__((swift_name("SKListVC")))
@protocol SVCSKListVC <SVCSKComponentVC>
@required
- (void)scrollToPositionPosition:(int32_t)position __attribute__((swift_name("scrollToPosition(position:)")));
@property NSArray<SVCSKListVCItem *> *items __attribute__((swift_name("items")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SKListVCItem")))
@interface SVCSKListVCItem : SVCBase
- (instancetype)initWithComponent:(id<SVCSKComponentVC>)component id:(id)id onSwipe:(void (^ _Nullable)(void))onSwipe __attribute__((swift_name("init(component:id:onSwipe:)"))) __attribute__((objc_designated_initializer));
- (id<SVCSKComponentVC>)component1 __attribute__((swift_name("component1()")));
- (id)component2 __attribute__((swift_name("component2()")));
- (void (^ _Nullable)(void))component3 __attribute__((swift_name("component3()")));
- (SVCSKListVCItem *)doCopyComponent:(id<SVCSKComponentVC>)component id:(id)id onSwipe:(void (^ _Nullable)(void))onSwipe __attribute__((swift_name("doCopy(component:id:onSwipe:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) id<SVCSKComponentVC> component __attribute__((swift_name("component")));
@property (readonly) id id __attribute__((swift_name("id")));
@property (readonly) void (^ _Nullable onSwipe)(void) __attribute__((swift_name("onSwipe")));
@end;

__attribute__((swift_name("SKLoaderVC")))
@protocol SVCSKLoaderVC <SVCSKComponentVC>
@required
@property BOOL visible __attribute__((swift_name("visible")));
@end;

__attribute__((swift_name("SKPagerVC")))
@protocol SVCSKPagerVC <SVCSKComponentVC>
@required
@property (readonly) void (^ _Nullable onSwipeToPage)(SVCInt *) __attribute__((swift_name("onSwipeToPage")));
@property NSArray<id<SVCSKScreenVC>> *screens __attribute__((swift_name("screens")));
@property int32_t selectedPageIndex __attribute__((swift_name("selectedPageIndex")));
@property (readonly) BOOL swipable __attribute__((swift_name("swipable")));
@end;

__attribute__((swift_name("SKPagerWithTabsVC")))
@protocol SVCSKPagerWithTabsVC <SVCSKComponentVC>
@required
@property NSArray<NSString *> *labels __attribute__((swift_name("labels")));
@property (readonly) id<SVCSKPagerVC> pager __attribute__((swift_name("pager")));
@end;

__attribute__((swift_name("SKScreenVC")))
@protocol SVCSKScreenVC <SVCSKComponentVC>
@required
@property void (^ _Nullable onBackPressed)(void) __attribute__((swift_name("onBackPressed")));
@end;

__attribute__((swift_name("SKStackVC")))
@protocol SVCSKStackVC <SVCSKComponentVC>
@required
@property SVCSKStackVCState *state __attribute__((swift_name("state")));
@end;

__attribute__((swift_name("SKStackVCState")))
@interface SVCSKStackVCState : SVCBase
- (instancetype)initWithScreens:(NSArray<id<SVCSKScreenVC>> *)screens transition:(id<SVCSKTransition> _Nullable)transition __attribute__((swift_name("init(screens:transition:)"))) __attribute__((objc_designated_initializer));
@property (readonly) NSArray<id<SVCSKScreenVC>> *screens __attribute__((swift_name("screens")));
@property (readonly) id<SVCSKTransition> _Nullable transition __attribute__((swift_name("transition")));
@end;

__attribute__((swift_name("SKVisiblityListener")))
@protocol SVCSKVisiblityListener
@required
- (void)onPause __attribute__((swift_name("onPause()")));
- (void)onResume __attribute__((swift_name("onResume()")));
@end;

__attribute__((swift_name("SKWebViewVC")))
@protocol SVCSKWebViewVC <SVCSKComponentVC>
@required
@property (readonly) SVCSKWebViewVCConfig *config __attribute__((swift_name("config")));
@property SVCSKWebViewVCBackRequest * _Nullable goBack __attribute__((swift_name("goBack")));
@property SVCSKWebViewVCOpenUrl * _Nullable openUrl __attribute__((swift_name("openUrl")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SKWebViewVCBackRequest")))
@interface SVCSKWebViewVCBackRequest : SVCBase
- (instancetype)initWithOnCantBack:(void (^ _Nullable)(void))onCantBack __attribute__((swift_name("init(onCantBack:)"))) __attribute__((objc_designated_initializer));
- (void (^ _Nullable)(void))component1 __attribute__((swift_name("component1()")));
- (SVCSKWebViewVCBackRequest *)doCopyOnCantBack:(void (^ _Nullable)(void))onCantBack __attribute__((swift_name("doCopy(onCantBack:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) void (^ _Nullable onCantBack)(void) __attribute__((swift_name("onCantBack")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SKWebViewVCConfig")))
@interface SVCSKWebViewVCConfig : SVCBase
- (instancetype)initWithUserAgent:(NSString * _Nullable)userAgent redirect:(NSArray<SVCSKWebViewVCRedirectParam *> *)redirect __attribute__((swift_name("init(userAgent:redirect:)"))) __attribute__((objc_designated_initializer));
- (NSString * _Nullable)component1 __attribute__((swift_name("component1()")));
- (NSArray<SVCSKWebViewVCRedirectParam *> *)component2 __attribute__((swift_name("component2()")));
- (SVCSKWebViewVCConfig *)doCopyUserAgent:(NSString * _Nullable)userAgent redirect:(NSArray<SVCSKWebViewVCRedirectParam *> *)redirect __attribute__((swift_name("doCopy(userAgent:redirect:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSArray<SVCSKWebViewVCRedirectParam *> *redirect __attribute__((swift_name("redirect")));
@property (readonly) NSString * _Nullable userAgent __attribute__((swift_name("userAgent")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SKWebViewVCOpenUrl")))
@interface SVCSKWebViewVCOpenUrl : SVCBase
- (instancetype)initWithUrl:(NSString *)url onFinished:(void (^ _Nullable)(void))onFinished javascriptOnFinished:(NSString * _Nullable)javascriptOnFinished onError:(void (^ _Nullable)(void))onError post:(NSDictionary<NSString *, NSString *> * _Nullable)post __attribute__((swift_name("init(url:onFinished:javascriptOnFinished:onError:post:)"))) __attribute__((objc_designated_initializer));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (void (^ _Nullable)(void))component2 __attribute__((swift_name("component2()")));
- (NSString * _Nullable)component3 __attribute__((swift_name("component3()")));
- (void (^ _Nullable)(void))component4 __attribute__((swift_name("component4()")));
- (NSDictionary<NSString *, NSString *> * _Nullable)component5 __attribute__((swift_name("component5()")));
- (SVCSKWebViewVCOpenUrl *)doCopyUrl:(NSString *)url onFinished:(void (^ _Nullable)(void))onFinished javascriptOnFinished:(NSString * _Nullable)javascriptOnFinished onError:(void (^ _Nullable)(void))onError post:(NSDictionary<NSString *, NSString *> * _Nullable)post __attribute__((swift_name("doCopy(url:onFinished:javascriptOnFinished:onError:post:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString * _Nullable javascriptOnFinished __attribute__((swift_name("javascriptOnFinished")));
@property (readonly) void (^ _Nullable onError)(void) __attribute__((swift_name("onError")));
@property (readonly) void (^ _Nullable onFinished)(void) __attribute__((swift_name("onFinished")));
@property (readonly) NSDictionary<NSString *, NSString *> * _Nullable post __attribute__((swift_name("post")));
@property (readonly) NSString *url __attribute__((swift_name("url")));
@end;

__attribute__((swift_name("SKWebViewVCRedirectParam")))
@interface SVCSKWebViewVCRedirectParam : SVCBase
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (BOOL)matchesUrl:(NSString *)url __attribute__((swift_name("matches(url:)")));
@property (readonly) SVCBoolean *(^onRedirect)(NSString *, NSDictionary<NSString *, NSString *> *) __attribute__((swift_name("onRedirect")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SKWebViewVCRedirectParam.Match")))
@interface SVCSKWebViewVCRedirectParamMatch : SVCSKWebViewVCRedirectParam
- (instancetype)initWithRegex:(SVCKotlinRegex *)regex onRedirect:(SVCBoolean *(^)(NSString *, NSDictionary<NSString *, NSString *> *))onRedirect __attribute__((swift_name("init(regex:onRedirect:)"))) __attribute__((objc_designated_initializer));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
- (BOOL)matchesUrl:(NSString *)url __attribute__((swift_name("matches(url:)")));
@property (readonly) SVCBoolean *(^onRedirect)(NSString *, NSDictionary<NSString *, NSString *> *) __attribute__((swift_name("onRedirect")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SKWebViewVCRedirectParam.Start")))
@interface SVCSKWebViewVCRedirectParamStart : SVCSKWebViewVCRedirectParam
- (instancetype)initWithStart:(NSString *)start onRedirect:(SVCBoolean *(^)(NSString *, NSDictionary<NSString *, NSString *> *))onRedirect __attribute__((swift_name("init(start:onRedirect:)"))) __attribute__((objc_designated_initializer));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
- (BOOL)matchesUrl:(NSString *)url __attribute__((swift_name("matches(url:)")));
@property (readonly) SVCBoolean *(^onRedirect)(NSString *, NSDictionary<NSString *, NSString *> *) __attribute__((swift_name("onRedirect")));
@end;

__attribute__((swift_name("SKAlertVC")))
@protocol SVCSKAlertVC <SVCSKComponentVC>
@required
@property SVCSKAlertVCShown * _Nullable state __attribute__((swift_name("state")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SKAlertVCButton")))
@interface SVCSKAlertVCButton : SVCBase
- (instancetype)initWithLabel:(NSString *)label action:(void (^ _Nullable)(void))action __attribute__((swift_name("init(label:action:)"))) __attribute__((objc_designated_initializer));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (void (^ _Nullable)(void))component2 __attribute__((swift_name("component2()")));
- (SVCSKAlertVCButton *)doCopyLabel:(NSString *)label action:(void (^ _Nullable)(void))action __attribute__((swift_name("doCopy(label:action:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) void (^ _Nullable action)(void) __attribute__((swift_name("action")));
@property (readonly) NSString *label __attribute__((swift_name("label")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SKAlertVCShown")))
@interface SVCSKAlertVCShown : SVCBase
- (instancetype)initWithTitle:(NSString * _Nullable)title message:(NSString * _Nullable)message cancelable:(BOOL)cancelable mainButton:(SVCSKAlertVCButton *)mainButton secondaryButton:(SVCSKAlertVCButton * _Nullable)secondaryButton __attribute__((swift_name("init(title:message:cancelable:mainButton:secondaryButton:)"))) __attribute__((objc_designated_initializer));
- (NSString * _Nullable)component1 __attribute__((swift_name("component1()")));
- (NSString * _Nullable)component2 __attribute__((swift_name("component2()")));
- (BOOL)component3 __attribute__((swift_name("component3()")));
- (SVCSKAlertVCButton *)component4 __attribute__((swift_name("component4()")));
- (SVCSKAlertVCButton * _Nullable)component5 __attribute__((swift_name("component5()")));
- (SVCSKAlertVCShown *)doCopyTitle:(NSString * _Nullable)title message:(NSString * _Nullable)message cancelable:(BOOL)cancelable mainButton:(SVCSKAlertVCButton *)mainButton secondaryButton:(SVCSKAlertVCButton * _Nullable)secondaryButton __attribute__((swift_name("doCopy(title:message:cancelable:mainButton:secondaryButton:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) BOOL cancelable __attribute__((swift_name("cancelable")));
@property (readonly) SVCSKAlertVCButton *mainButton __attribute__((swift_name("mainButton")));
@property (readonly) NSString * _Nullable message __attribute__((swift_name("message")));
@property (readonly) SVCSKAlertVCButton * _Nullable secondaryButton __attribute__((swift_name("secondaryButton")));
@property (readonly) NSString * _Nullable title __attribute__((swift_name("title")));
@end;

__attribute__((swift_name("SKBottomSheetVC")))
@protocol SVCSKBottomSheetVC <SVCSKComponentVC>
@required
@property SVCSKBottomSheetVCShown * _Nullable state __attribute__((swift_name("state")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SKBottomSheetVCShown")))
@interface SVCSKBottomSheetVCShown : SVCBase
- (instancetype)initWithScreen:(id<SVCSKScreenVC>)screen onDismiss:(void (^ _Nullable)(void))onDismiss expanded:(BOOL)expanded __attribute__((swift_name("init(screen:onDismiss:expanded:)"))) __attribute__((objc_designated_initializer));
- (id<SVCSKScreenVC>)component1 __attribute__((swift_name("component1()")));
- (void (^ _Nullable)(void))component2 __attribute__((swift_name("component2()")));
- (BOOL)component3 __attribute__((swift_name("component3()")));
- (SVCSKBottomSheetVCShown *)doCopyScreen:(id<SVCSKScreenVC>)screen onDismiss:(void (^ _Nullable)(void))onDismiss expanded:(BOOL)expanded __attribute__((swift_name("doCopy(screen:onDismiss:expanded:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) BOOL expanded __attribute__((swift_name("expanded")));
@property (readonly) void (^ _Nullable onDismiss)(void) __attribute__((swift_name("onDismiss")));
@property (readonly) id<SVCSKScreenVC> screen __attribute__((swift_name("screen")));
@end;

__attribute__((swift_name("SKSnackBarVC")))
@protocol SVCSKSnackBarVC <SVCSKComponentVC>
@required
@property SVCSKSnackBarVCShown * _Nullable state __attribute__((swift_name("state")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SKSnackBarVCAction")))
@interface SVCSKSnackBarVCAction : SVCBase
- (instancetype)initWithLabel:(NSString *)label action:(void (^)(void))action __attribute__((swift_name("init(label:action:)"))) __attribute__((objc_designated_initializer));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (void (^)(void))component2 __attribute__((swift_name("component2()")));
- (SVCSKSnackBarVCAction *)doCopyLabel:(NSString *)label action:(void (^)(void))action __attribute__((swift_name("doCopy(label:action:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) void (^action)(void) __attribute__((swift_name("action")));
@property (readonly) NSString *label __attribute__((swift_name("label")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SKSnackBarVCShown")))
@interface SVCSKSnackBarVCShown : SVCBase
- (instancetype)initWithMessage:(NSString *)message action:(SVCSKSnackBarVCAction * _Nullable)action onTop:(BOOL)onTop __attribute__((swift_name("init(message:action:onTop:)"))) __attribute__((objc_designated_initializer));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (SVCSKSnackBarVCAction * _Nullable)component2 __attribute__((swift_name("component2()")));
- (BOOL)component3 __attribute__((swift_name("component3()")));
- (SVCSKSnackBarVCShown *)doCopyMessage:(NSString *)message action:(SVCSKSnackBarVCAction * _Nullable)action onTop:(BOOL)onTop __attribute__((swift_name("doCopy(message:action:onTop:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) SVCSKSnackBarVCAction * _Nullable action __attribute__((swift_name("action")));
@property (readonly) NSString *message __attribute__((swift_name("message")));
@property (readonly) BOOL onTop __attribute__((swift_name("onTop")));
@end;

__attribute__((swift_name("SKWindowPopupVC")))
@protocol SVCSKWindowPopupVC <SVCSKComponentVC>
@required
@property SVCSKWindowPopupVCShown * _Nullable state __attribute__((swift_name("state")));
@end;

__attribute__((swift_name("SKWindowPopupVCBehavior")))
@interface SVCSKWindowPopupVCBehavior : SVCBase
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SKWindowPopupVCCancelable")))
@interface SVCSKWindowPopupVCCancelable : SVCSKWindowPopupVCBehavior
- (instancetype)initWithOnDismiss:(void (^ _Nullable)(void))onDismiss __attribute__((swift_name("init(onDismiss:)"))) __attribute__((objc_designated_initializer));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
@property (readonly) void (^ _Nullable onDismiss)(void) __attribute__((swift_name("onDismiss")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SKWindowPopupVCNotCancelable")))
@interface SVCSKWindowPopupVCNotCancelable : SVCSKWindowPopupVCBehavior
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
+ (instancetype)notCancelable __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) SVCSKWindowPopupVCNotCancelable *shared __attribute__((swift_name("shared")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SKWindowPopupVCShown")))
@interface SVCSKWindowPopupVCShown : SVCBase
- (instancetype)initWithComponent:(id<SVCSKComponentVC>)component behavior:(SVCSKWindowPopupVCBehavior *)behavior __attribute__((swift_name("init(component:behavior:)"))) __attribute__((objc_designated_initializer));
- (id<SVCSKComponentVC>)component1 __attribute__((swift_name("component1()")));
- (SVCSKWindowPopupVCBehavior *)component2 __attribute__((swift_name("component2()")));
- (SVCSKWindowPopupVCShown *)doCopyComponent:(id<SVCSKComponentVC>)component behavior:(SVCSKWindowPopupVCBehavior *)behavior __attribute__((swift_name("doCopy(component:behavior:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) SVCSKWindowPopupVCBehavior *behavior __attribute__((swift_name("behavior")));
@property (readonly) id<SVCSKComponentVC> component __attribute__((swift_name("component")));
@end;

__attribute__((swift_name("SKButtonVC")))
@protocol SVCSKButtonVC <SVCSKComponentVC>
@required
@property SVCBoolean * _Nullable enabled __attribute__((swift_name("enabled")));
@property SVCBoolean * _Nullable hidden __attribute__((swift_name("hidden")));
@property NSString * _Nullable label __attribute__((swift_name("label")));
@property void (^ _Nullable onTap)(void) __attribute__((swift_name("onTap")));
@end;

__attribute__((swift_name("SKComboVC")))
@protocol SVCSKComboVC <SVCSKComponentVC>
@required
@property NSArray<SVCSKComboVCChoice *> *choices __attribute__((swift_name("choices")));
@property BOOL dropDownDisplayed __attribute__((swift_name("dropDownDisplayed")));
@property SVCBoolean * _Nullable enabled __attribute__((swift_name("enabled")));
@property SVCBoolean * _Nullable hidden __attribute__((swift_name("hidden")));
@property (readonly) NSString * _Nullable hint __attribute__((swift_name("hint")));
@property (readonly) void (^ _Nullable onSelected)(id _Nullable) __attribute__((swift_name("onSelected")));
@property SVCSKComboVCChoice * _Nullable selected __attribute__((swift_name("selected")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SKComboVCChoice")))
@interface SVCSKComboVCChoice : SVCBase
- (instancetype)initWithData:(id _Nullable)data text:(NSString *)text strikethrough:(BOOL)strikethrough colored:(BOOL)colored inputText:(NSString *)inputText __attribute__((swift_name("init(data:text:strikethrough:colored:inputText:)"))) __attribute__((objc_designated_initializer));
@property (readonly) BOOL colored __attribute__((swift_name("colored")));
@property (readonly) id _Nullable data __attribute__((swift_name("data")));
@property (readonly) NSString *inputText __attribute__((swift_name("inputText")));
@property (readonly) BOOL strikethrough __attribute__((swift_name("strikethrough")));
@property (readonly) NSString *text __attribute__((swift_name("text")));
@end;

__attribute__((swift_name("SKImageButtonVC")))
@protocol SVCSKImageButtonVC <SVCSKComponentVC>
@required
@property SVCBoolean * _Nullable enabled __attribute__((swift_name("enabled")));
@property SVCBoolean * _Nullable hidden __attribute__((swift_name("hidden")));
@property SVCIcon *icon __attribute__((swift_name("icon")));
@property void (^ _Nullable onTap)(void) __attribute__((swift_name("onTap")));
@end;

__attribute__((swift_name("SKInputVC")))
@protocol SVCSKInputVC <SVCSKComponentVC>
@required
- (void)requestFocus __attribute__((swift_name("requestFocus()")));
@property SVCBoolean * _Nullable enabled __attribute__((swift_name("enabled")));
@property NSString * _Nullable error __attribute__((swift_name("error")));
@property SVCBoolean * _Nullable hidden __attribute__((swift_name("hidden")));
@property NSString * _Nullable hint __attribute__((swift_name("hint")));
@property (readonly) SVCInt * _Nullable maxSize __attribute__((swift_name("maxSize")));
@property (readonly) void (^ _Nullable onDone)(NSString * _Nullable) __attribute__((swift_name("onDone")));
@property (readonly) void (^ _Nullable onFocusLost)(void) __attribute__((swift_name("onFocusLost")));
@property (readonly) void (^onInputText)(NSString * _Nullable) __attribute__((swift_name("onInputText")));
@property NSString * _Nullable text __attribute__((swift_name("text")));
@property (readonly) SVCSKInputVCType * _Nullable type __attribute__((swift_name("type")));
@end;

__attribute__((swift_name("SKInputVCType")))
@interface SVCSKInputVCType : SVCBase
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SKInputVCType.EMail")))
@interface SVCSKInputVCTypeEMail : SVCSKInputVCType
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
+ (instancetype)eMail __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) SVCSKInputVCTypeEMail *shared __attribute__((swift_name("shared")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SKInputVCType.LongText")))
@interface SVCSKInputVCTypeLongText : SVCSKInputVCType
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
+ (instancetype)longText __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) SVCSKInputVCTypeLongText *shared __attribute__((swift_name("shared")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SKInputVCType.Normal")))
@interface SVCSKInputVCTypeNormal : SVCSKInputVCType
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
+ (instancetype)normal __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) SVCSKInputVCTypeNormal *shared __attribute__((swift_name("shared")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SKInputVCType.Number")))
@interface SVCSKInputVCTypeNumber : SVCSKInputVCType
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
+ (instancetype)number __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) SVCSKInputVCTypeNumber *shared __attribute__((swift_name("shared")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SKInputVCType.NumberPassword")))
@interface SVCSKInputVCTypeNumberPassword : SVCSKInputVCType
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
+ (instancetype)numberPassword __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) SVCSKInputVCTypeNumberPassword *shared __attribute__((swift_name("shared")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SKInputVCType.Phone")))
@interface SVCSKInputVCTypePhone : SVCSKInputVCType
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
+ (instancetype)phone __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) SVCSKInputVCTypePhone *shared __attribute__((swift_name("shared")));
@end;

__attribute__((swift_name("SKInputWithSuggestionsVC")))
@protocol SVCSKInputWithSuggestionsVC <SVCSKComboVC>
@required
- (void)requestFocus __attribute__((swift_name("requestFocus()")));
@property (readonly) void (^onInputText)(NSString * _Nullable) __attribute__((swift_name("onInputText")));
@end;

__attribute__((swift_name("SKSimpleInputVC")))
@protocol SVCSKSimpleInputVC <SVCSKInputVC>
@required
@end;

__attribute__((swift_name("CoreViewInjector")))
@protocol SVCCoreViewInjector
@required
- (id<SVCSKAlertVC>)alert __attribute__((swift_name("alert()")));
- (id<SVCSKBottomSheetVC>)bottomSheet __attribute__((swift_name("bottomSheet()")));
- (id<SVCSKButtonVC>)buttonOnTapInitial:(void (^ _Nullable)(void))onTapInitial labelInitial:(NSString * _Nullable)labelInitial enabledInitial:(SVCBoolean * _Nullable)enabledInitial hiddenInitial:(SVCBoolean * _Nullable)hiddenInitial __attribute__((swift_name("button(onTapInitial:labelInitial:enabledInitial:hiddenInitial:)")));
- (id<SVCSKComboVC>)comboHint:(NSString * _Nullable)hint onSelected:(void (^ _Nullable)(id _Nullable))onSelected choicesInitial:(NSArray<SVCSKComboVCChoice *> *)choicesInitial selectedInitial:(SVCSKComboVCChoice * _Nullable)selectedInitial enabledInitial:(SVCBoolean * _Nullable)enabledInitial hiddenInitial:(SVCBoolean * _Nullable)hiddenInitial dropDownDisplayedInitial:(BOOL)dropDownDisplayedInitial __attribute__((swift_name("combo(hint:onSelected:choicesInitial:selectedInitial:enabledInitial:hiddenInitial:dropDownDisplayedInitial:)")));
- (id<SVCSKFrameVC>)frameScreens:(NSSet<id<SVCSKScreenVC>> *)screens screenInitial:(id<SVCSKScreenVC> _Nullable)screenInitial __attribute__((swift_name("frame(screens:screenInitial:)")));
- (id<SVCSKImageButtonVC>)imageButtonOnTapInitial:(void (^ _Nullable)(void))onTapInitial iconInitial:(SVCIcon *)iconInitial enabledInitial:(SVCBoolean * _Nullable)enabledInitial hiddenInitial:(SVCBoolean * _Nullable)hiddenInitial __attribute__((swift_name("imageButton(onTapInitial:iconInitial:enabledInitial:hiddenInitial:)")));
- (id<SVCSKInputVC>)inputOnInputText:(void (^)(NSString * _Nullable))onInputText type:(SVCSKInputVCType * _Nullable)type maxSize:(SVCInt * _Nullable)maxSize onFocusLost:(void (^ _Nullable)(void))onFocusLost onDone:(void (^ _Nullable)(NSString * _Nullable))onDone hintInitial:(NSString * _Nullable)hintInitial textInitial:(NSString * _Nullable)textInitial errorInitial:(NSString * _Nullable)errorInitial hiddenInitial:(SVCBoolean * _Nullable)hiddenInitial enabledInitial:(SVCBoolean * _Nullable)enabledInitial __attribute__((swift_name("input(onInputText:type:maxSize:onFocusLost:onDone:hintInitial:textInitial:errorInitial:hiddenInitial:enabledInitial:)")));
- (id<SVCSKSimpleInputVC>)inputSimpleOnInputText:(void (^)(NSString * _Nullable))onInputText type:(SVCSKInputVCType * _Nullable)type maxSize:(SVCInt * _Nullable)maxSize onFocusLost:(void (^ _Nullable)(void))onFocusLost onDone:(void (^ _Nullable)(NSString * _Nullable))onDone hintInitial:(NSString * _Nullable)hintInitial textInitial:(NSString * _Nullable)textInitial errorInitial:(NSString * _Nullable)errorInitial hiddenInitial:(SVCBoolean * _Nullable)hiddenInitial enabledInitial:(SVCBoolean * _Nullable)enabledInitial __attribute__((swift_name("inputSimple(onInputText:type:maxSize:onFocusLost:onDone:hintInitial:textInitial:errorInitial:hiddenInitial:enabledInitial:)")));
- (id<SVCSKInputWithSuggestionsVC>)inputWithSuggestionsHint:(NSString * _Nullable)hint onSelected:(void (^ _Nullable)(id _Nullable))onSelected choicesInitial:(NSArray<SVCSKComboVCChoice *> *)choicesInitial selectedInitial:(SVCSKComboVCChoice * _Nullable)selectedInitial enabledInitial:(SVCBoolean * _Nullable)enabledInitial hiddenInitial:(SVCBoolean * _Nullable)hiddenInitial dropDownDisplayedInitial:(BOOL)dropDownDisplayedInitial onInputText:(void (^)(NSString * _Nullable))onInputText __attribute__((swift_name("inputWithSuggestions(hint:onSelected:choicesInitial:selectedInitial:enabledInitial:hiddenInitial:dropDownDisplayedInitial:onInputText:)")));
- (id<SVCSKLoaderVC>)loader __attribute__((swift_name("loader()")));
- (id<SVCSKPagerVC>)pagerScreens:(NSArray<id<SVCSKScreenVC>> *)screens onSwipeToPage:(void (^ _Nullable)(SVCInt *))onSwipeToPage initialSelectedPageIndex:(int32_t)initialSelectedPageIndex swipable:(BOOL)swipable __attribute__((swift_name("pager(screens:onSwipeToPage:initialSelectedPageIndex:swipable:)")));
- (id<SVCSKPagerWithTabsVC>)pagerWithTabsPager:(id<SVCSKPagerVC>)pager labels:(NSArray<NSString *> *)labels __attribute__((swift_name("pagerWithTabs(pager:labels:)")));
- (id<SVCSKStackVC>)rootStack __attribute__((swift_name("rootStack()")));
- (id<SVCSKBoxVC>)skBoxItemsInitial:(NSArray<id<SVCSKComponentVC>> *)itemsInitial hiddenInitial:(SVCBoolean * _Nullable)hiddenInitial __attribute__((swift_name("skBox(itemsInitial:hiddenInitial:)")));
- (id<SVCSKListVC>)skListVertical:(BOOL)vertical reverse:(BOOL)reverse nbColumns:(SVCInt * _Nullable)nbColumns animate:(BOOL)animate animateItem:(BOOL)animateItem __attribute__((swift_name("skList(vertical:reverse:nbColumns:animate:animateItem:)")));
- (id<SVCSKSnackBarVC>)snackBar __attribute__((swift_name("snackBar()")));
- (id<SVCSKStackVC>)stack __attribute__((swift_name("stack()")));
- (id<SVCSKWebViewVC>)webViewConfig:(SVCSKWebViewVCConfig *)config openUrlInitial:(SVCSKWebViewVCOpenUrl * _Nullable)openUrlInitial __attribute__((swift_name("webView(config:openUrlInitial:)")));
- (id<SVCSKWindowPopupVC>)windowPopup __attribute__((swift_name("windowPopup()")));
@end;

__attribute__((swift_name("KotlinFunction")))
@protocol SVCKotlinFunction
@required
@end;

__attribute__((swift_name("KotlinSuspendFunction0")))
@protocol SVCKotlinSuspendFunction0 <SVCKotlinFunction>
@required

/**
 @note This method converts instances of CancellationException to errors.
 Other uncaught Kotlin exceptions are fatal.
*/
- (void)invokeWithCompletionHandler:(void (^)(id _Nullable_result, NSError * _Nullable))completionHandler __attribute__((swift_name("invoke(completionHandler:)")));
@end;

__attribute__((swift_name("KotlinThrowable")))
@interface SVCKotlinThrowable : SVCBase
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(SVCKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(SVCKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));
- (SVCKotlinArray<NSString *> *)getStackTrace __attribute__((swift_name("getStackTrace()")));
- (void)printStackTrace __attribute__((swift_name("printStackTrace()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) SVCKotlinThrowable * _Nullable cause __attribute__((swift_name("cause")));
@property (readonly) NSString * _Nullable message __attribute__((swift_name("message")));
- (NSError *)asError __attribute__((swift_name("asError()")));
@end;

__attribute__((swift_name("KotlinException")))
@interface SVCKotlinException : SVCKotlinThrowable
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(SVCKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(SVCKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
@end;

__attribute__((swift_name("KotlinRuntimeException")))
@interface SVCKotlinRuntimeException : SVCKotlinException
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(SVCKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(SVCKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
@end;

__attribute__((swift_name("KotlinIllegalStateException")))
@interface SVCKotlinIllegalStateException : SVCKotlinRuntimeException
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(SVCKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(SVCKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
@end;

__attribute__((swift_name("KotlinCancellationException")))
@interface SVCKotlinCancellationException : SVCKotlinIllegalStateException
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(SVCKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(SVCKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinUnit")))
@interface SVCKotlinUnit : SVCBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)unit __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) SVCKotlinUnit *shared __attribute__((swift_name("shared")));
- (NSString *)description __attribute__((swift_name("description()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinRegex")))
@interface SVCKotlinRegex : SVCBase
- (instancetype)initWithPattern:(NSString *)pattern __attribute__((swift_name("init(pattern:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithPattern:(NSString *)pattern option:(SVCKotlinRegexOption *)option __attribute__((swift_name("init(pattern:option:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithPattern:(NSString *)pattern options:(NSSet<SVCKotlinRegexOption *> *)options __attribute__((swift_name("init(pattern:options:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) SVCKotlinRegexCompanion *companion __attribute__((swift_name("companion")));
- (BOOL)containsMatchInInput:(id)input __attribute__((swift_name("containsMatchIn(input:)")));
- (id<SVCKotlinMatchResult> _Nullable)findInput:(id)input startIndex:(int32_t)startIndex __attribute__((swift_name("find(input:startIndex:)")));
- (id<SVCKotlinSequence>)findAllInput:(id)input startIndex:(int32_t)startIndex __attribute__((swift_name("findAll(input:startIndex:)")));
- (id<SVCKotlinMatchResult> _Nullable)matchAtInput:(id)input index:(int32_t)index __attribute__((swift_name("matchAt(input:index:)")));
- (id<SVCKotlinMatchResult> _Nullable)matchEntireInput:(id)input __attribute__((swift_name("matchEntire(input:)")));
- (BOOL)matchesInput:(id)input __attribute__((swift_name("matches(input:)")));
- (BOOL)matchesAtInput:(id)input index:(int32_t)index __attribute__((swift_name("matchesAt(input:index:)")));
- (NSString *)replaceInput:(id)input transform:(id (^)(id<SVCKotlinMatchResult>))transform __attribute__((swift_name("replace(input:transform:)")));
- (NSString *)replaceInput:(id)input replacement:(NSString *)replacement __attribute__((swift_name("replace(input:replacement:)")));
- (NSString *)replaceFirstInput:(id)input replacement:(NSString *)replacement __attribute__((swift_name("replaceFirst(input:replacement:)")));
- (NSArray<NSString *> *)splitInput:(id)input limit:(int32_t)limit __attribute__((swift_name("split(input:limit:)")));
- (id<SVCKotlinSequence>)splitToSequenceInput:(id)input limit:(int32_t)limit __attribute__((swift_name("splitToSequence(input:limit:)")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSSet<SVCKotlinRegexOption *> *options __attribute__((swift_name("options")));
@property (readonly) NSString *pattern __attribute__((swift_name("pattern")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinArray")))
@interface SVCKotlinArray<T> : SVCBase
+ (instancetype)arrayWithSize:(int32_t)size init:(T _Nullable (^)(SVCInt *))init __attribute__((swift_name("init(size:init:)")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (T _Nullable)getIndex:(int32_t)index __attribute__((swift_name("get(index:)")));
- (id<SVCKotlinIterator>)iterator __attribute__((swift_name("iterator()")));
- (void)setIndex:(int32_t)index value:(T _Nullable)value __attribute__((swift_name("set(index:value:)")));
@property (readonly) int32_t size __attribute__((swift_name("size")));
@end;

__attribute__((swift_name("KotlinComparable")))
@protocol SVCKotlinComparable
@required
- (int32_t)compareToOther:(id _Nullable)other __attribute__((swift_name("compareTo(other:)")));
@end;

__attribute__((swift_name("KotlinEnum")))
@interface SVCKotlinEnum<E> : SVCBase <SVCKotlinComparable>
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) SVCKotlinEnumCompanion *companion __attribute__((swift_name("companion")));
- (int32_t)compareToOther:(E)other __attribute__((swift_name("compareTo(other:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *name __attribute__((swift_name("name")));
@property (readonly) int32_t ordinal __attribute__((swift_name("ordinal")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinRegexOption")))
@interface SVCKotlinRegexOption : SVCKotlinEnum<SVCKotlinRegexOption *>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) SVCKotlinRegexOption *ignoreCase __attribute__((swift_name("ignoreCase")));
@property (class, readonly) SVCKotlinRegexOption *multiline __attribute__((swift_name("multiline")));
@property (class, readonly) SVCKotlinRegexOption *literal __attribute__((swift_name("literal")));
@property (class, readonly) SVCKotlinRegexOption *unixLines __attribute__((swift_name("unixLines")));
@property (class, readonly) SVCKotlinRegexOption *comments __attribute__((swift_name("comments")));
@property (class, readonly) SVCKotlinRegexOption *dotMatchesAll __attribute__((swift_name("dotMatchesAll")));
@property (class, readonly) SVCKotlinRegexOption *canonEq __attribute__((swift_name("canonEq")));
+ (SVCKotlinArray<SVCKotlinRegexOption *> *)values __attribute__((swift_name("values()")));
@property (readonly) int32_t mask __attribute__((swift_name("mask")));
@property (readonly) int32_t value __attribute__((swift_name("value")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinRegex.Companion")))
@interface SVCKotlinRegexCompanion : SVCBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) SVCKotlinRegexCompanion *shared __attribute__((swift_name("shared")));
- (NSString *)escapeLiteral:(NSString *)literal __attribute__((swift_name("escape(literal:)")));
- (NSString *)escapeReplacementLiteral:(NSString *)literal __attribute__((swift_name("escapeReplacement(literal:)")));
- (SVCKotlinRegex *)fromLiteralLiteral:(NSString *)literal __attribute__((swift_name("fromLiteral(literal:)")));
@end;

__attribute__((swift_name("KotlinMatchResult")))
@protocol SVCKotlinMatchResult
@required
- (id<SVCKotlinMatchResult> _Nullable)next __attribute__((swift_name("next()")));
@property (readonly) SVCKotlinMatchResultDestructured *destructured __attribute__((swift_name("destructured")));
@property (readonly) NSArray<NSString *> *groupValues __attribute__((swift_name("groupValues")));
@property (readonly) id<SVCKotlinMatchGroupCollection> groups __attribute__((swift_name("groups")));
@property (readonly) SVCKotlinIntRange *range __attribute__((swift_name("range")));
@property (readonly) NSString *value_ __attribute__((swift_name("value_")));
@end;

__attribute__((swift_name("KotlinSequence")))
@protocol SVCKotlinSequence
@required
- (id<SVCKotlinIterator>)iterator __attribute__((swift_name("iterator()")));
@end;

__attribute__((swift_name("KotlinIterator")))
@protocol SVCKotlinIterator
@required
- (BOOL)hasNext __attribute__((swift_name("hasNext()")));
- (id _Nullable)next __attribute__((swift_name("next()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinEnumCompanion")))
@interface SVCKotlinEnumCompanion : SVCBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) SVCKotlinEnumCompanion *shared __attribute__((swift_name("shared")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinMatchResultDestructured")))
@interface SVCKotlinMatchResultDestructured : SVCBase
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (NSString *)component10 __attribute__((swift_name("component10()")));
- (NSString *)component2 __attribute__((swift_name("component2()")));
- (NSString *)component3 __attribute__((swift_name("component3()")));
- (NSString *)component4 __attribute__((swift_name("component4()")));
- (NSString *)component5 __attribute__((swift_name("component5()")));
- (NSString *)component6 __attribute__((swift_name("component6()")));
- (NSString *)component7 __attribute__((swift_name("component7()")));
- (NSString *)component8 __attribute__((swift_name("component8()")));
- (NSString *)component9 __attribute__((swift_name("component9()")));
- (NSArray<NSString *> *)toList __attribute__((swift_name("toList()")));
@property (readonly) id<SVCKotlinMatchResult> match __attribute__((swift_name("match")));
@end;

__attribute__((swift_name("KotlinIterable")))
@protocol SVCKotlinIterable
@required
- (id<SVCKotlinIterator>)iterator __attribute__((swift_name("iterator()")));
@end;

__attribute__((swift_name("KotlinCollection")))
@protocol SVCKotlinCollection <SVCKotlinIterable>
@required
- (BOOL)containsElement:(id _Nullable)element __attribute__((swift_name("contains(element:)")));
- (BOOL)containsAllElements:(id)elements __attribute__((swift_name("containsAll(elements:)")));
- (BOOL)isEmpty __attribute__((swift_name("isEmpty()")));
@property (readonly) int32_t size __attribute__((swift_name("size")));
@end;

__attribute__((swift_name("KotlinMatchGroupCollection")))
@protocol SVCKotlinMatchGroupCollection <SVCKotlinCollection>
@required
- (SVCKotlinMatchGroup * _Nullable)getIndex:(int32_t)index __attribute__((swift_name("get(index:)")));
@end;

__attribute__((swift_name("KotlinIntProgression")))
@interface SVCKotlinIntProgression : SVCBase <SVCKotlinIterable>
@property (class, readonly, getter=companion) SVCKotlinIntProgressionCompanion *companion __attribute__((swift_name("companion")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (BOOL)isEmpty __attribute__((swift_name("isEmpty()")));
- (SVCKotlinIntIterator *)iterator __attribute__((swift_name("iterator()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) int32_t first __attribute__((swift_name("first")));
@property (readonly) int32_t last __attribute__((swift_name("last")));
@property (readonly) int32_t step __attribute__((swift_name("step")));
@end;

__attribute__((swift_name("KotlinClosedRange")))
@protocol SVCKotlinClosedRange
@required
- (BOOL)containsValue:(id)value __attribute__((swift_name("contains(value:)")));
- (BOOL)isEmpty __attribute__((swift_name("isEmpty()")));
@property (readonly) id endInclusive __attribute__((swift_name("endInclusive")));
@property (readonly) id start __attribute__((swift_name("start")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinIntRange")))
@interface SVCKotlinIntRange : SVCKotlinIntProgression <SVCKotlinClosedRange>
- (instancetype)initWithStart:(int32_t)start endInclusive:(int32_t)endInclusive __attribute__((swift_name("init(start:endInclusive:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) SVCKotlinIntRangeCompanion *companion __attribute__((swift_name("companion")));
- (BOOL)containsValue:(SVCInt *)value __attribute__((swift_name("contains(value:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (BOOL)isEmpty __attribute__((swift_name("isEmpty()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) SVCInt *endInclusive __attribute__((swift_name("endInclusive")));
@property (readonly) SVCInt *start __attribute__((swift_name("start")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinMatchGroup")))
@interface SVCKotlinMatchGroup : SVCBase
- (instancetype)initWithValue:(NSString *)value range:(SVCKotlinIntRange *)range __attribute__((swift_name("init(value:range:)"))) __attribute__((objc_designated_initializer));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (SVCKotlinIntRange *)component2 __attribute__((swift_name("component2()")));
- (SVCKotlinMatchGroup *)doCopyValue:(NSString *)value range:(SVCKotlinIntRange *)range __attribute__((swift_name("doCopy(value:range:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) SVCKotlinIntRange *range __attribute__((swift_name("range")));
@property (readonly) NSString *value __attribute__((swift_name("value")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinIntProgression.Companion")))
@interface SVCKotlinIntProgressionCompanion : SVCBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) SVCKotlinIntProgressionCompanion *shared __attribute__((swift_name("shared")));
- (SVCKotlinIntProgression *)fromClosedRangeRangeStart:(int32_t)rangeStart rangeEnd:(int32_t)rangeEnd step:(int32_t)step __attribute__((swift_name("fromClosedRange(rangeStart:rangeEnd:step:)")));
@end;

__attribute__((swift_name("KotlinIntIterator")))
@interface SVCKotlinIntIterator : SVCBase <SVCKotlinIterator>
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (SVCInt *)next __attribute__((swift_name("next()")));
- (int32_t)nextInt __attribute__((swift_name("nextInt()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinIntRange.Companion")))
@interface SVCKotlinIntRangeCompanion : SVCBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) SVCKotlinIntRangeCompanion *shared __attribute__((swift_name("shared")));
@property (readonly) SVCKotlinIntRange *EMPTY __attribute__((swift_name("EMPTY")));
@end;

#pragma pop_macro("_Nullable_result")
#pragma clang diagnostic pop
NS_ASSUME_NONNULL_END
