using System;

using System.Collections.Generic;
using Android.App;
using Android.OS;
using Android.Widget;
using System.Net;
using System.Collections.Specialized;
using Android.Util;
using System.Xml;
using System.IO;

namespace Dictionary
{
	[Activity (Label = "Dictionary Picker")]			
	public class DictionaryPickerActivity : Activity
	{
		ListView DictionaryList;

		DictionaryListAdapter Adapter;

		WebClient Client;
		Uri Uri;

		//DictionaryListAdapter Adapter;
		
		protected override void OnCreate (Bundle bundle)
		{
			base.OnCreate (bundle);
			Log.Info ("Dictionary", "DictionaryPickerActivity");

			Client = new WebClient();
			Uri = new Uri ("http://services.aonaware.com/DictService/DictService.asmx/Define");

			DictionaryList = FindViewById<ListView> (Resource.Id.DictionaryList);

			LoadDictionaries ();
		}

		private void PopulateWordList( object sender, UploadValuesCompletedEventArgs e)
		{
			List<DictionaryResult> Results = new List<DictionaryResult> ();
			XmlDocument Response = new XmlDocument ();

			Response.Load (new MemoryStream (e.Result));

			XmlNodeList Dictionaries = Response.GetElementsByTagName ("Dictionary");

			foreach (XmlNode d in Dictionaries) {
				Results.Add (new DictionaryResult {
					Id = Results.Count,
					DictId = d ["Id"].InnerText,
					Name = d ["Name"].InnerText,
				    Enabled = true
				});
			}
				
			// TODO eerst nog de List filteren met de dictionary list van wordresult om dicts die niet voorkomen uit te grayen
			Adapter = new DictionaryListAdapter (this, Results);
			DictionaryList.Adapter = Adapter;
		}

		private void LoadDictionaries(){
			Log.Info ("Dictionary", "LoadDictionaries()");

			Client.UploadValuesCompleted += PopulateWordList;
			Client.CancelAsync ();
			try{
				Client.UploadValuesAsync (Uri, null);
			}catch(Exception e){
				// fuck the police
				Log.Info ("DictionaryPickerActivity", "UploadValuesAsync() -> EXCEPTION :(" );
			}
		}
	}

	public class DictionaryResult{
		public long Id{ get; set; }
		public String DictId{ get; set; }
		public String Name{ get; set; }
		public bool Enabled{ get; set; }
	}
}

