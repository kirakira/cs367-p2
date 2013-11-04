#!/bin/sh

FILES=("CircularLinkedList.java" "ElementNotFoundException.java" "JosephusMain.java")
GIVEN=("CircularListADT.java" "ListNode.java")
GRADER="CircularLinkedListTester.java"
MAIN="JosephusMain"

containsElement () {
    local e
    for e in "${@:2}"; do
        if [ $e == $1 ]; then
            return 1
        fi
    done
    return 0
}

function copy_files {
    name=$(basename $1)
    echo "Running tests for $name..."

    mkdir -p cs367_grading_p2/$name
    rm -rf cs367_grading/p2/$name

    cp -t cs367_grading_p2/$name/ /p/course/cs367-skrentny/handin/$name/p2/*
}

function grade {
    name=$(basename $1)

    mkdir -p cs367_grading_p2/$name
    cd cs367_grading_p2/$name

    found_all_files=1

    for f in ${FILES[*]}; do
        if [ ! -f $f ]; then
            echo "Could not find $f" >> Output.txt
            found_all_files=0
        fi
    done

    for f in *; do
        if [ $f == "README.txt" ]; then
	        echo "Found a README. This student worked with a partner, view README." >> Output.txt
	        echo "This student worked with a partner. View their README file."
        else
            containsElement $f ${FILES[*]}
            if [ $? == 0 ]; then
                echo "Found extra file: $f" >> Output.txt
            fi
        fi
    done

    if [ $found_all_files == 0 ]; then
        echo "Due to lack of necessary source files, skipped tests for $name"
        echo "Due to lack of necessary source files, skipped tests for $name" >> Output.txt
    else
        for f in ${GIVEN[*]}; do
            cp /p/course/cs367-skrentny/public/html/assignments/p2/files/$f .
        done
        cp /p/course/cs367-skrentny/public/html/assignments/p2/grading/$GRADER .

        javac *.java &>> Output.txt

        if [ $? == 0 ]; then
            echo "Compiled successfullly"

            echo "Running pretests"
            /p/course/cs367-skrentny/public/html/assignments/p2/grading/timeout.sh -t 10 java ${GRADER%.java} >> Output.txt
            if [ $? != 0 ]; then
                echo "Time limit exceeded; there may be an infinite loop in the code" >> Output.txt
            fi

            echo "" >> Output.txt
            for txt in /p/course/cs367-skrentny/public/html/assignments/p2/grading/cases/*.txt; do
                inp=${txt%.txt}.in
                if [ -f $inp ]; then
                    caseName=$(basename $inp)
                    caseName=${caseName%.*}
                    oup=$caseName.out

                    echo "====Case: $caseName====" >> Output.txt
                    echo "Running case $caseName"
                    /p/course/cs367-skrentny/public/html/assignments/p2/grading/timeout.sh -t 10 java $MAIN $txt < $inp &> $oup

                    if [ $? != 0 ]; then
                        echo "Verdict: TIMED OUT or PROGRAM CRASHED. See $oup for details." >> Output.txt
                    else
                        ans=${txt%.txt}.ans
                        if [ -f $ans ]; then
                            diff $oup $ans > $caseName.diff
                            if [ -s $caseName.diff ]; then
                                echo "Verdict: WRONG" >> Output.txt
                                echo "See $oup for full output. Following are diff result:" >> Output.txt
                                cat $caseName.diff >> Output.txt
                            else
                                echo "Verdict: correct" >> Output.txt
                            fi
                        else
                            echo "Full output:" >> Output.txt
                            cat $oup >> Output.txt
                        fi
                    fi

                    echo "" >> Output.txt
                fi
            done
        else
            echo "Failed compilation"
            echo "Due to compilation errors, skipped all of the tests" >> Output.txt
        fi
    fi

    echo "Finished testing for $name"
    echo ""

    cd ..
    cd ..
}

cd ~
if [ ! -z $1 ] ; then
    grade $1
else
    if [[ -d cs367_grading_p2 ]]; then
        rm -rf cs367_grading_p2
    fi
    mkdir cs367_grading_p2

    for file in /p/course/cs367-skrentny/handin/*; do
        if [[ -d $file ]]; then
            copy_files $file
            grade $file
        fi
    done
fi
