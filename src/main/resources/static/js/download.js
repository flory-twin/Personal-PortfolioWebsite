const download = {
	displayAlertAndPdfWarningMessage : () => {
	    alert( `
If nothing displays, please check your downloads list or folder. Some browsers are configured to download files instead of displaying them inline.`);
		location.assign('/professional/KFlory.Resume.pdf');
	},
	displayAlertAndDownload: () => {
	    alert( `
Not all browsers will report whether your download succeeded. If no status message appears after this alert is cleared, please check your downloads list or folder.`);
		location.assign('/professional/KFlory.Resume.pdf');
	}
}