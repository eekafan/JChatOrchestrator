define ([],function () {
	var  isodate = function (localdatetime) {
		var timezone_offset_min = localdatetime.getTimezoneOffset();
		var offset_hrs = parseInt(Math.abs(timezone_offset_min/60));
		var offset_min = Math.abs(timezone_offset_min%60);
		var timezone_standard = undefined;

		if(offset_hrs < 10)
		offset_hrs = '0' + offset_hrs;

		if(offset_min < 10)
		offset_min = '0' + offset_min;

		//Add an opposite sign to the offset
		//If offset is 0, it means timezone is UTC
		if(timezone_offset_min < 0)
		timezone_standard = '+' + offset_hrs + ':' + offset_min;
		else if(timezone_offset_min > 0)
		timezone_standard = '-' + offset_hrs + ':' + offset_min;
		else if(timezone_offset_min == 0)
		timezone_standard = 'Z';
		
		var utcstring = localdatetime.toJSON();
		return utcstring.replace('Z',timezone_standard);
	}

	
	return isodate;
});