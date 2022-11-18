package es.unizar.urlshortener.core

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

// The threat types to be checked.
enum class ThreatType {
    THREAT_TYPE_UNSPECIFIED,	        //Unknown.
    MALWARE,	                        //Malware threat type.
    SOCIAL_ENGINEERING,	                //Social engineering threat type.
    UNWANTED_SOFTWARE,	                //Unwanted software threat type.
    POTENTIALLY_HARMFUL_APPLICATION     //Potentially harmful application threat type.
}

// The platform types to be checked.
enum class PlatformType {
    PLATFORM_TYPE_UNSPECIFIED,	//Unknown platform.
    WINDOWS,	                //Threat posed to Windows.
    LINUX,	                    //Threat posed to Linux.
    ANDROID,	                //Threat posed to Android.
    OSX,	                    //Threat posed to OS X.
    IOS,	                    //Threat posed to iOS.
    ANY_PLATFORM,	            //Threat posed to at least one of the defined platforms.
    ALL_PLATFORMS,	            //Threat posed to all defined platforms.
    CHROME	                    //Threat posed to Chrome.
}

// The entry types to be checked.
enum class ThreatEntryType {
    THREAT_ENTRY_TYPE_UNSPECIFIED,	//Unspecified.
    URL,	                        //A URL.
    EXECUTABLE	                    //An executable program.
}

// An individual threat; for example, a malicious URL or its hash representation. Only one of these fields should be set.
enum class ThreatEntryRequestType   {
    HASH,
    URL,
    DIGEST
}

@JsonInclude(JsonInclude.Include.NON_NULL)
class ThreatEntry(value: String, type: ThreatEntryRequestType) {
    @JsonProperty("hash")
    var hash: String? = null
    @JsonProperty("url")
    var url: String? = null
    @JsonProperty("digest")
    var digest: String? = null

    init {
        setOneValue(value, type)
    }

    private fun setOneValue(value: String, type: ThreatEntryRequestType){
        if (type==ThreatEntryRequestType.DIGEST){
            digest = value;
        }
        if (type==ThreatEntryRequestType.URL){
            url = value;
        }
        if (type==ThreatEntryRequestType.HASH){
            hash = value;
        }
        arrayOf<ThreatEntryRequestType>(ThreatEntryRequestType.HASH, ThreatEntryRequestType.HASH)
    }
}

/**** REQUEST ***/
// Finds the threat entries that match the Safe Browsing lists.
@JsonInclude(JsonInclude.Include.NON_NULL)
class ThreatMatchesFindRequestBody (
        @JsonProperty("client")
        var client : ClientInfo? = null,
        @JsonProperty("threatInfo")
        var threatInfo: ThreatInfo? = null
)

@JsonInclude(JsonInclude.Include.NON_NULL)
class ClientInfo (
        @JsonProperty("clientId")
        var clientId : String? = null,
        @JsonProperty("clientVersion")
        var clientVersion: String? = null
)

@JsonInclude(JsonInclude.Include.NON_NULL)
class ThreatInfo (
        @JsonProperty("threatTypes")
        var threatTypes : List<ThreatType>? = null,
        @JsonProperty("platformTypes")
        var platformTypes: List<PlatformType>? = null,
        @JsonProperty("threatEntryTypes")
        var threatEntryTypes : List<ThreatEntryType>? = null,
        @JsonProperty("threatEntries")
        var threatEntries: List<ThreatEntry>? = null
)


/**** RESPONSE ***/
@JsonInclude(JsonInclude.Include.NON_NULL)
class ThreatMatchesFindResponseBody (
        @JsonProperty("matches")
        var matches : List<ThreatMatch>? = null
)

@JsonInclude(JsonInclude.Include.NON_NULL)
class ThreatMatch (){
    @JsonProperty("threatType")
    var threatType : ThreatType? = null
    @JsonProperty("platformType")
    var platformType: PlatformType? = null
    @JsonProperty("threatEntryType")
    var threatEntryType: ThreatEntryType? = null
    @JsonProperty("cacheDuration")
    var cacheDuration: String? = null
}