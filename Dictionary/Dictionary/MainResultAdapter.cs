using System;
using Android.Widget;
using Android.App;
using System.Collections.Generic;
using Android.Views;
using Android.Util;

namespace Dictionary
{
	public class MainResultAdapter : BaseAdapter 
	{
		Activity activity;
		List<WordResult> results;

		public MainResultAdapter( Activity a, List<WordResult> r )
		{
			Log.Info ("WordListAdapter", "Initialized - results to show: " + r.Count);

			activity = a;
			results = r;
		}

		public override int Count {
			get { return results.Count; }
		}

		public override Java.Lang.Object GetItem (int position) {
			// could wrap a Contact in a Java.Lang.Object
			// to return it here if needed
			return null;
		}

		public override long GetItemId (int position) {
			return results [position].Id;
		}

		public override View GetView (int position, View convertView, ViewGroup parent)
		{
			var view = convertView ?? activity.LayoutInflater.Inflate (
				Resource.Layout.ResultItem, parent, false);
			var dictionary = view.FindViewById<TextView> (Resource.Id.Dictionary);
			var definition = view.FindViewById<TextView> (Resource.Id.Definition);

			dictionary.Text = results [position].Dictionary;
			definition.Text = results [position].Definition;

			return view;
		}
	}
}
