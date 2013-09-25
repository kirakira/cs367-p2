#!/bin/sh

cd ~

if [ ! -z $1 ] ; then
echo "Running tests for "$1" . . ."
      cd ~/cs367_grading_p2/$1
	  rm -rf Output.txt
	  found_all_files=true
	  if [ ! -f LinkedList.java ]; then
		echo "Could not find LinkedList.java." >> Output.txt
		found_all_files=false
	  fi
	  if [ ! -f MovieQueueMain.java ]; then
		echo "Could not find MovieQueueMain.java." >> Output.txt
		found_all_files=false
	  fi
	  if [ ! -f InvalidListPositionException.java ]; then
		echo "Could not find InvalidListPositionException.java." >> Output.txt
		found_all_files=false
	  fi
	  found_readme=false
	  if [ -f README.txt ]; then
		found_readme=true
	  fi
	  if [ ! $found_all_files ] &&  [ ! $found_readme ]; then
	    echo "Could not find a README either. This student either named all files incorrectly or did not submit anything." >> Output.txt
		echo "Can't find the required files for this student. Check their folder."
		studentsNocompile[$nocompile]=$1
		nocompile=$nocompile+1
	  fi
	  
	  if $found_readme ; then
	    echo "Found a README. This student worked with a partner, view README." >> Output.txt
	    echo "This student worked with a partner. View their README file."
	  fi
	  
	  
	  if $found_all_files ; then
		  compiled=true
	      echo "Running tests... " >> Output.txt
		  echo " " >> Output.txt
		  ## Compiling ListNode
		  javac ListNode.java >> Output.txt
		  if [ ! -f ListNode.class ] ; then
			echo "ListNode.java failed to compile. The student should not have modified this file." >> Output.txt
			compiled=false
		  fi
		  echo "================= Compiling InvalidListPositionException.java =================" >> Output.txt
		  javac InvalidListPositionException.java >> Output.txt
		  if [ ! -f InvalidListPositionException.class ] ; then
			echo "InvalidListPositionException.java failed to compile. View student's code." >> Output.txt
			compiled=false
		  else
		    echo "InvalidListPositionException.java compiled successfully." >> Output.txt
		  fi
		  ## Compiling ListADT
		  javac ListADT.java >> Output.txt
		  if [ ! -f ListADT.class ] ; then
			echo "ListADT.java failed to compile. The student should not have modified this file." >> Output.txt
			compiled=false
		  fi
		  echo "================= Compiling LinkedList.java =================" >> Output.txt
		  javac LinkedList.java >> Output.txt
		  if [ ! -f LinkedList.class ] ; then
			echo "LinkedList.java failed to compile. View student's code." >> Output.txt
			compiled=false
		  else
		    echo "LinkedList.java compiled successfully." >> Output.txt
		  fi
		  echo " " >> Output.txt
		  echo "============= Compiling MovieQueueMain.java ===============" >> Output.txt
		  javac MovieQueueMain.java >> Output.txt
		  if [ ! -f MovieQueueMain.class ]; then
		    echo "MovieQueueMain.java failed to compile. View student's code." >> Output.txt
			compiled=false
		  else
		    echo "MovieQueueMain.java compiled successfully." >> Output.txt
		  fi
		  echo " " >> Output.txt
		  echo "============= Compiling MovieQueueTester.java ===============" >> Output.txt
		  javac MovieQueueTester.java >> Output.txt
		  if [ ! -f MovieQueueTester.class ]; then
		    echo "MovieQueueTester.java failed to compile. This may be because the student's LinkedList class is missing methods; view their code." >> Output.txt
			compiled=false
		  else
		    echo "MovieQueueTester.java compiled successfully." >> Output.txt
		  fi
		  echo " " >> Output.txt
		  if $compiled ; then
		    echo "This student's files compiled successfully, running tests . . ."
		    echo "================== Running Tests ========================" >> Output.txt
		    /p/course/cs367-skrentny/public/html/assignments/p2/grading/timeout.sh -t 40 java MovieQueueTester >> Output.txt
		  fi
	  fi
	  echo $1" complete."
	  cd ..	
else
	if [[ -d cs367_grading_p2 ]]; then
		rm -rf cs367_grading_p2
	fi
	mkdir cs367_grading_p2
	cd /p/course/cs367-skrentny/handin

	for file in *; do
	   if [ -d $file ]; then
		  mkdir ~/cs367_grading_p2/$file
		  cp $file/p2/* ~/cs367_grading_p2/$file
	   fi
	done

	cd ~/cs367_grading_p2

	nocompile=0
	
	for file in *; do
	   if [ -d $file ]; then
		  echo "Running tests for "$file" . . ."
		  cd $file
		  found_all_files=true
		  if [ ! -f LinkedList.java ]; then
			echo "Could not find LinkedList.java." >> Output.txt
			found_all_files=false
		  fi
		  if [ ! -f MovieQueueMain.java ]; then
			echo "Could not find MovieQueueMain.java." >> Output.txt
			found_all_files=false
		  fi
		  found_readme=false
		  if [ -f README.txt ]; then
			found_readme=true
		  fi
		  if [ ! $found_all_files ] &&  [ ! $found_readme ]; then
			echo "Could not find a README either. This student either named some files incorrectly or did not submit everything they needed to." >> Output.txt
			echo "Can't find the required files for this student. Check their folder."
		  fi
		  
		  if $found_readme ; then
			echo "Found a README. This student worked with a partner, view README." >> Output.txt
			echo "This student worked with a partner. View their README file."
		  fi
		  
		  
		  if $found_all_files ; then
			  compiled=true
			  echo "Running tests... " >> Output.txt
			  echo " " >> Output.txt
			  cp /p/course/cs367-skrentny/public/html/assignments/p2/grading/files/* .
			  ## Compiling ListNode
			  javac ListNode.java >> Output.txt
			  if [ ! -f ListNode.class ] ; then
			    echo "ListNode.java failed to compile. The student should not have modified this file." >> Output.txt
				compiled=false
			  fi
			  echo "================= Compiling InvalidListPositionException.java =================" >> Output.txt
			  javac InvalidListPositionException.java >> Output.txt
			  if [ ! -f InvalidListPositionException.class ] ; then
				echo "InvalidListPositionException.java failed to compile. View student's code." >> Output.txt
				compiled=false
			  else
				echo "InvalidListPositionException.java compiled successfully." >> Output.txt
			  fi
			  ## Compiling ListADT
			  javac ListADT.java >> Output.txt
			  if [ ! -f ListADT.class ] ; then
				echo "ListADT.java failed to compile. The student should not have modified this file." >> Output.txt
				compiled=false
			  fi
			  echo "============== Compiling LinkedList.java ===============" >> Output.txt
			  javac LinkedList.java >> Output.txt
			  if [ ! -f LinkedList.class ] ; then
				echo "LinkedList.java failed to compile. View student's code." >> Output.txt
				compiled=false
			  else
				echo "LinkedList.java compiled successfully." >> Output.txt
			  fi
			  echo " " >> Output.txt
			  echo "============= Compiling MovieQueueMain.java ===============" >> Output.txt
			  javac MovieQueueMain.java >> Output.txt
			  if [ ! -f MovieQueueMain.class ]; then
				echo "MovieQueueMain.java failed to compile. View student's code." >> Output.txt
				compiled=false
			  else
				echo "MovieQueueMain.java compiled successfully." >> Output.txt
			  fi
			  echo " " >> Output.txt
			  echo "============= Compiling MovieQueueTester.java ===============" >> Output.txt
			  javac MovieQueueTester.java >> Output.txt
			  if [ ! -f MovieQueueTester.class ]; then
				echo "MovieQueueTester.java failed to compile. This may be because the student's LinkedList class is missing methods; view their code." >> Output.txt
				compiled=false
			  else
				echo "MovieQueueTester.java compiled successfully." >> Output.txt
			  fi
			  echo " " >> Output.txt
			  if $compiled ; then
				echo "This student's files compiled successfully, running tests . . ."
				echo "================== Running Tests ========================" >> Output.txt
				/p/course/cs367-skrentny/public/html/assignments/p2/grading/timeout.sh -t 40 java MovieQueueTester >> Output.txt
			  else
			    notCompiled[$nocompile]=$file
				nocompile=$nocompile+1
			  fi
		  fi
		  echo $file" complete."
		  cd ..
		fi
	done
	
	echo "Students whose code did not compile:" >> DidNotCompile.txt
	echo ${notCompiled[@]} >> DidNotCompile.txt
	echo " " >> DidNotCompile.txt
fi

cd ~
exit
