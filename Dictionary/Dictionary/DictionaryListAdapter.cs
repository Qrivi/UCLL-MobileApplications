using System;
using Android.Widget;
using Android.App;
using System.Collections.Generic;
using Android.Views;
using Android.Util;

namespace Dictionary
{
	public class DictionaryListAdapter : BaseAdapter 
	{
		Activity Activity;
		List<DictionaryResult> Dictionaries;

		public DictionaryListAdapter( Activity a, List<DictionaryResult> d )
		{
			Log.Info ("DictionaryListAdapter", "Initialized - results to show: " + d.Count);
			
			Activity = a;
			Dictionaries = d;
		}

		public override int Count {
			get { return Dictionaries.Count; }
		}

		public override Java.Lang.Object GetItem (int position) {
			// could wrap a Contact in a Java.Lang.Object
			// to return it here if needed
			return null;
		}

		public override long GetItemId (int position) {
			return Dictionaries [position].Id;
		}

		public override View GetView (int position, View convertView, ViewGroup parent)
		{
			var view = convertView ?? Activity.LayoutInflater.Inflate (
				Resource.Layout.DictionaryItem, parent, false);
			var dictionaryName = view.FindViewById<TextView> (Resource.Id.DictionaryName);

			dictionaryName.Text = Dictionaries [position].Name;

			return view;
		}
	}
}

