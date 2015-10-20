﻿using System;

using Android.App;
using Android.Content;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using Android.OS;
using Android.Util;

namespace Chronometer
{
	[Activity (Label = "@string/app_name", MainLauncher = true, Icon = "@drawable/icon")]
	public class MainActivity : Activity
	{
		
		TextView timeView;
		Button startStopButton;
		Button resetButton;

		ISharedPreferences data;
		ISharedPreferencesEditor editor;

		Handler timeHandler;
		DateTime startTime;
		TimeSpan accumulatedTime;

		bool isRunning = false;

		protected override void OnCreate (Bundle bundle)
		{
			base.OnCreate (bundle);

			Log.Info ("Chronometer", "Hello World");

			// Create preference file and editor
			data = Application.Context.GetSharedPreferences("Data", FileCreationMode.Private);
			editor = data.Edit ();

			// Set our view from the "main" layout resource
			SetContentView (Resource.Layout.Main);

			// Get our button from the layout resource,
			// and attach an event to it
			timeView = FindViewById<TextView> (Resource.Id.timeView);
			startStopButton = FindViewById<Button> (Resource.Id.startStopButton);
			resetButton = FindViewById<Button> (Resource.Id.resetButton);
			
			startStopButton.Click += (sender, e) => StartStopHandler();
			resetButton.Click += (sender, e) => ResetHandler();

			timeHandler = new Handler ();
			startTime = new DateTime ();
			accumulatedTime = new TimeSpan ();
		}

		protected override void OnResume(){
			base.OnResume ();

			//accumulatedTime = TimeSpan.Parse(data.GetString ("count", "0" ));
			//updateTimeView ();
		}

		protected override void OnPause(){
			base.OnPause ();

			isRunning = true;
			StartStopHandler ();

			editor.PutString ("count", accumulatedTime.ToString() );
			editor.Apply ();
		}

		/* protected override void OnSaveInstanceState(Bundle outState) {
			base.OnSaveInstanceState (outState);
			outState.PutInt ("count", count );

		}

		protected override void OnRestoreInstanceState (Bundle savedInstanceState) {
			base.OnRestoreInstanceState (savedInstanceState);
			count = (int) savedInstanceState.Get ("count");
			ShowCount ();
		}*/

		private void ResetHandler(){
			startTime = new DateTime();
			accumulatedTime = new TimeSpan ();
			timeView.Text = accumulatedTime.ToString(@"mm\:ss\,fff");
		}

		private void StartStopHandler(){
			Log.Info ("Chronometer", "startStopHandler()");

			if (isRunning) {
				isRunning = false;
				resetButton.Enabled = true;
				startStopButton.Text = GetString (Resource.String.start);
			} else {
				isRunning = true;
				resetButton.Enabled = false;
				startStopButton.Text = GetString (Resource.String.stop);

				if (startTime == DateTime.MinValue)
					startTime = DateTime.Now;
				TriggerUpdate ();
			}
		}

		private void TriggerUpdate(){
			if(isRunning) timeHandler.PostDelayed (Update, 1);
		}
		private void Update(){
			accumulatedTime = DateTime.Now.Subtract( startTime );
			timeView.Text = accumulatedTime.ToString(@"mm\:ss\,fff");

			TriggerUpdate ();
		}
	}
}