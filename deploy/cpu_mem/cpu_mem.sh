#!/bin/bash
#demo: sh cpu_mem.sh packagename delaytime
apkname=$1
delay=$2


device_num=`adb devices | grep "device"$ | wc -l`
for ((j=1;j<=$device_num;j++));
    do
    device_serialname=` adb devices | grep "device"$ | awk '{print $1}' | sed -n "${j}p"`
    rm -rf ./reports/${device_serialname}_*
    rm -rf ./reports/${device_serialname}_*
    cp -rf cpu.html ./reports/${device_serialname}_cpu.html
    htmlname=./reports/${device_serialname}_cpu.html
    cp -rf mem.html ./reports/${device_serialname}_mem.html
    htmlname_mem=./reports/${device_serialname}_mem.html
    date=`date`
    echo $date
    sed -i '' '/\<\/body\>/i\  
    '"$date"'\
    ' $htmlname_mem
    sed -i '' '/\<\/body\>/i\  
    '"$date"'\
    ' $htmlname


    done

for((i=0;i<1000;i++));
do
        device_num=`adb devices | grep "device"$ | wc -l`
        for ((j=1;j<=$device_num;j++));
        do
        device_serialname=` adb devices | grep "device"$ | awk '{print $1}' | sed -n "${j}p"`
        # cp -rf index.html ${device_serialname}_cpu.html
        htmlname=./reports/${device_serialname}_cpu.html
        # echo $htmlname
        # echo  ${device_serialname}
        # echo $device_num
        htmlname_mem=./reports/${device_serialname}_mem.html
        # echo $htmlname_mem

        if [ "$device_num" -eq 1 ]
            then
            echo $device_num"++++++++___________"
            
            # echo "["$i"," >> ./reports/${device_serialname}_cpu.log
            
            echo "deviceid--->"$device_serialname"----->获取数据条数 $i"
            cpu_S=`adb -s ${device_serialname} shell top -n 1 -d 0 | grep $1  |awk '{s=$5} END {print s}'`
            cpu_S_3=`adb -s ${device_serialname} shell top -n 1 -d 0 | grep $1  |awk '{s=$4} END {print s}'`
            cpu_S_7=`adb -s ${device_serialname} shell top -n 1 -d 0 | grep $1  |awk '{s=$6} END {print s}'`
            echo $cpu_S
            if [[ "$cpu_S" == "S" ]]; then
                #statements
                # echo '我的cpu 在4位'
                echo "["$i","`adb -s ${device_serialname} shell top -n 1 -d $2 | grep $1  |awk '{s+=$4} END {print s}'`"]," >> ./reports/${device_serialname}_cpu.log &
            elif [[ "$cpu_S_3" == "S" ]]; then
                echo "["$i","`adb -s ${device_serialname} shell top -n 1 -d $2 | grep $1  |awk '{s+=$3} END {print s}'`"]," >> ./reports/${device_serialname}_cpu.log &
            elif [[ "$cpu_S_7" == "S" ]]; then
                echo "["$i","`adb -s ${device_serialname} shell top -n 1 -d $2 | grep $1  |awk '{s+=$5} END {print s}'`"]," >> ./reports/${device_serialname}_cpu.log &
            else
                echo "[$i,0]," >> ${device_serialname}_cpu.log
                sleep $delay 
            fi


            cpu=`awk 'BEGIN{RS=""}NF{$1=$1;print RS$0}' ./reports/${device_serialname}_cpu.log`
            # echo $cpu
            echo "myChart.setDataArray([" $cpu "], 'blue');" > ./reports/${device_serialname}_log_tmp.log
            cpu_tmp=`more ./reports/${device_serialname}_log_tmp.log`
            # echo $cpu_tmp
            # sed -i '' '/new/a\  
            # '"$cpu_tmp"'\
            # ' $htmlname 
            python merge.py $htmlname ./reports/${device_serialname}_log_tmp.log
            mem_judge=`adb -s ${device_serialname} shell dumpsys meminfo $1`
            # echo $mem_judge
            if [[ $mem_judge =~ 'No process' ]]; then
               echo 'no process'
               echo "[$i,0],"  >> ./reports/${device_serialname}_memN.log &
               echo "[$i,0],"  >> ./reports/${device_serialname}_memD.log &

            elif [[ ! `adb  -s ${device_serialname} shell dumpsys meminfo $1 | grep 'Native Heap'` ]]; then
               echo "["$i","`adb -s ${device_serialname} shell dumpsys meminfo $1 | grep 'Native' |grep -v ':' |awk '{print $6}'`"]," >> ./reports/${device_serialname}_memN.log &
               echo "["$i","`adb -s ${device_serialname} shell dumpsys meminfo $1| grep 'Dalvik' |awk '{print $6}'`"]," >> ./reports/${device_serialname}_memD.log &
            
            elif [[ `adb  -s ${device_serialname} shell dumpsys meminfo $1 | grep 'Native Heap'|awk '{print $3}'` = "0" ]]; then
               echo "["$i","`adb -s ${device_serialname} shell dumpsys meminfo $1 | grep 'Native' |grep -v ':' |awk '{print $8}'`"]," >> ./reports/${device_serialname}_memN.log &
               echo "["$i","`adb -s ${device_serialname} shell dumpsys meminfo $1| grep 'Dalvik' |awk '{print $8}'`"]," >> ./reports/${device_serialname}_memD.log &
            
            else
               echo "["$i","`adb -s ${device_serialname} shell dumpsys meminfo $1 | grep 'Native Heap' |grep -v ':' |awk '{print $3}'`"]," >> ./reports/${device_serialname}_memN.log &
               echo "["$i","`adb -s ${device_serialname} shell dumpsys meminfo $1| grep 'Dalvik Heap' |awk '{print $3}'`"]," >> ./reports/${device_serialname}_memD.log &
            fi

            
            sleep $delay
            mem_N=`awk 'BEGIN{RS=""}NF{$1=$1;print RS$0}' ./reports/${device_serialname}_memN.log`
            mem_D=`awk 'BEGIN{RS=""}NF{$1=$1;print RS$0}' ./reports/${device_serialname}_memD.log`
            echo "myChart.setDataArray([" $mem_N "], 'blue');" > ./reports/${device_serialname}_log_memN_tmp.log
            echo "myChart.setDataArray([" $mem_D "], 'green');" > ./reports/${device_serialname}_log_memD_tmp.log
            memN_tmp=`more ./reports/${device_serialname}_log_memN_tmp.log`
            memD_tmp=`more ./reports/${device_serialname}_log_memD_tmp.log`
            python merge.py $htmlname_mem ./reports/${device_serialname}_log_memN_tmp.log
            python merge.py $htmlname_mem ./reports/${device_serialname}_log_memD_tmp.log
            # sed -i '' '/new/a\  
            # '"$memN_tmp"'\
            # ' $htmlname_mem
            # sed -i '' '/new/a\  
            # '"$memD_tmp"'\
            # ' $htmlname_mem


            # cpu=`awk 'BEGIN{RS=""}NF{$1=$1;print RS$0}' 034c6aa3094b6c54_cpu.log`
            # # echo $cpu
            # echo "myChart.setDataArray([" $cpu "], 'blue');" > 034c6aa3094b6c54_log_tmp.log
            # cpu_tmp=`more 034c6aa3094b6c54_log_tmp.log`
            # echo $cpu_tmp
            # sed -i '' '/new/a\  
            # '"$cpu_tmp"'\
            # ' 034c6aa3094b6c54_cpu.html



            continue
        else
            if [ "$j" -eq "$device_num" ]
                then
                   cpu_S_7=`adb -s ${device_serialname} shell top -n 1 -d 0 | grep $1  |awk '{s=$6} END {print s}'`
                   cpu_S=`adb -s ${device_serialname} shell top -n 1 -d 0 | grep $1  |awk '{s=$5} END {print s}'`
                   cpu_S_3=`adb -s ${device_serialname} shell top -n 1 -d 0 | grep $1  |awk '{s=$4} END {print s}'`
                   # echo $cpu_S
                   if [[ "$cpu_S" == "S" ]]; then
                        #statements
                        # echo '我的cpu 在4位'
                        echo "["$i","`adb -s ${device_serialname} shell top -n 1 -d $2 | grep $1  |awk '{s+=$4} END {print s}'`"]," >> ${device_serialname}_cpu.log &
                   elif [[ "$cpu_S_3" == "S" ]]; then
                        echo "["$i","`adb -s ${device_serialname} shell top -n 1 -d $2 | grep $1  |awk '{s+=$3} END {print s}'`"]," >> ${device_serialname}_cpu.log &
                   elif [[ "$cpu_S_7" == "S" ]]; then
                        echo "["$i","`adb -s ${device_serialname} shell top -n 1 -d $2 | grep $1  |awk '{s+=$5} END {print s}'`"]," >> ${device_serialname}_cpu.log &
                   else
                        echo "[$i,0]," >> ./reports/${device_serialname}_cpu.log
                        sleep $delay 
                   fi
                   cpu=`awk 'BEGIN{RS=""}NF{$1=$1;print RS$0}' ./reports/${device_serialname}_cpu.log`
                    # echo $cpu
                   echo "myChart.setDataArray([" $cpu "], 'blue');" > ./reports/${device_serialname}_log_tmp.log
                   cpu_tmp=`more ./reports/${device_serialname}_log_tmp.log`
                   # echo $cpu_tmp
                    # sed -i '' '/new/a\  
                    # '"$cpu_tmp"'\
                    # ' $htmlname 
                    python merge.py $htmlname ./reports/${device_serialname}_log_tmp.log


                    mem_judge=`adb -s ${device_serialname} shell dumpsys meminfo $1`
                    # echo $mem_judge
                    if [[ $mem_judge =~ 'No process' ]]; then
                       echo 'no process'
                       echo "[$i,0],"  >> ./reports/${device_serialname}_memN.log &
                       echo "[$i,0],"  >> ./reports/${device_serialname}_memD.log &

                    elif [[ ! `adb  -s ${device_serialname} shell dumpsys meminfo $1 | grep 'Native Heap'` ]]; then
                       echo "["$i","`adb -s ${device_serialname} shell dumpsys meminfo $1 | grep 'Native' |grep -v ':' |awk '{print $6}'`"]," >> ./reports/${device_serialname}_memN.log &
                       echo "["$i","`adb -s ${device_serialname} shell dumpsys meminfo $1| grep 'Dalvik' |awk '{print $6}'`"]," >> ./reports/${device_serialname}_memD.log &
                    
                    elif [[ `adb  -s ${device_serialname} shell dumpsys meminfo $1 | grep 'Native Heap'|awk '{print $3}'` = "0" ]]; then
                    echo "["$i","`adb -s ${device_serialname} shell dumpsys meminfo $1 | grep 'Native' |grep -v ':' |awk '{print $8}'`"]," >> ./reports/${device_serialname}_memN.log &
                    echo "["$i","`adb -s ${device_serialname} shell dumpsys meminfo $1| grep 'Dalvik' |awk '{print $8}'`"]," >> ./reports/${device_serialname}_memD.log &

                    else
                       echo "["$i","`adb -s ${device_serialname} shell dumpsys meminfo $1 | grep 'Native Heap' |grep -v ':' |awk '{print $3}'`"]," >> ./reports/${device_serialname}_memN.log &
                       echo "["$i","`adb -s ${device_serialname} shell dumpsys meminfo $1| grep 'Dalvik Heap' |awk '{print $3}'`"]," >> ./reports/${device_serialname}_memD.log &
                    fi
                    sleep $delay
                    mem_N=`awk 'BEGIN{RS=""}NF{$1=$1;print RS$0}' ./reports/${device_serialname}_memN.log`
                    mem_D=`awk 'BEGIN{RS=""}NF{$1=$1;print RS$0}' ./reports/${device_serialname}_memD.log`
                    echo "myChart.setDataArray([" $mem_N "], 'blue');" > ./reports/${device_serialname}_log_memN_tmp.log
                    echo "myChart.setDataArray([" $mem_D "], 'green');" > ./reports/${device_serialname}_log_memD_tmp.log
                    memN_tmp=`more ./reports/${device_serialname}_log_memN_tmp.log`
                    memD_tmp=`more ./reports/${device_serialname}_log_memD_tmp.log`
                    python merge.py $htmlname_mem ./reports/${device_serialname}_log_memN_tmp.log
                    python merge.py $htmlname_mem ./reports/${device_serialname}_log_memD_tmp.log
                    # sed -i '' '/new/a\  
                    # '"$memN_tmp"'\
                    # ' $htmlname_mem
                    # sed -i '' '/new/a\  
                    # '"$memD_tmp"'\
                    # ' $htmlname_mem
            else
                cpu_S_7=`adb -s ${device_serialname} shell top -n 1 -d 0 | grep $1  |awk '{s=$6} END {print s}'`
                cpu_S=`adb -s ${device_serialname} shell top -n 1 -d 0 | grep $1  |awk '{s=$5} END {print s}'`
                cpu_S_3=`adb -s ${device_serialname} shell top -n 1 -d 0 | grep $1  |awk '{s=$4} END {print s}'`
                # echo $cpu_S
                if [[ "$cpu_S" == "S" ]]; then
                    #statements
                    # echo '我的cpu 在4位'
                    echo "["$i","`adb -s ${device_serialname} shell top -n 1 -d $2 | grep $1  |awk '{s+=$4} END {print s}'`"]," >> ${device_serialname}_cpu.log &
                elif [[ "$cpu_S_3" == "S" ]]; then
                    echo "["$i","`adb -s ${device_serialname} shell top -n 1 -d $2 | grep $1  |awk '{s+=$3} END {print s}'`"]," >> ${device_serialname}_cpu.log &
                elif [[ "$cpu_S_7" == "S" ]]; then
                    echo "["$i","`adb -s ${device_serialname} shell top -n 1 -d $2 | grep $1  |awk '{s+=$5} END {print s}'`"]," >> ${device_serialname}_cpu.log &
                else
                    echo "[$i,0]," >> ./reports/${device_serialname}_cpu.log
                    sleep $delay 
                fi
                cpu=`awk 'BEGIN{RS=""}NF{$1=$1;print RS$0}' ./reports/${device_serialname}_cpu.log`
                # echo $cpu
                echo "myChart.setDataArray([" $cpu "], 'blue');" > ./reports/${device_serialname}_log_tmp.log
                cpu_tmp=`more ./reports/${device_serialname}_log_tmp.log`
                # echo $cpu_tmp
                # sed -i '' '/new/a\  
                # '"$cpu_tmp"'\
                # ' $htmlname 
                python merge.py $htmlname ./reports/${device_serialname}_log_tmp.log
                
                mem_judge=`adb -s ${device_serialname} shell dumpsys meminfo $1`
                # echo $mem_judge
                if [[ $mem_judge =~ 'No process' ]]; then
                   echo 'no process'
                   echo "[$i,0],"  >> ./reports/${device_serialname}_memN.log &
                   echo "[$i,0],"  >> ./reports/${device_serialname}_memD.log &

                elif [[ ! `adb  -s ${device_serialname} shell dumpsys meminfo $1 | grep 'Native Heap'` ]]; then
                   echo "["$i","`adb -s ${device_serialname} shell dumpsys meminfo $1 | grep 'Native' |grep -v ':' |awk '{print $6}'`"]," >> ${device_serialname}_memN.log &
                   echo "["$i","`adb -s ${device_serialname} shell dumpsys meminfo $1| grep 'Dalvik' |awk '{print $6}'`"]," >> ${device_serialname}_memD.log & 
                
                elif [[ `adb  -s ${device_serialname} shell dumpsys meminfo $1 | grep 'Native Heap'|awk '{print $3}'` = "0" ]]; then
                  echo "["$i","`adb -s ${device_serialname} shell dumpsys meminfo $1 | grep 'Native' |grep -v ':' |awk '{print $8}'`"]," >> ${device_serialname}_memN.log &
                  echo "["$i","`adb -s ${device_serialname} shell dumpsys meminfo $1| grep 'Dalvik' |awk '{print $8}'`"]," >> ${device_serialname}_memD.log &  
 
                else
                   echo "["$i","`adb -s ${device_serialname} shell dumpsys meminfo $1 | grep 'Native Heap' |grep -v ':' |awk '{print $3}'`"]," >> ${device_serialname}_memN.log &
                   echo "["$i","`adb -s ${device_serialname} shell dumpsys meminfo $1| grep 'Dalvik Heap' |awk '{print $3}'`"]," >> ${device_serialname}_memD.log & 
                fi
                sleep $delay
                mem_N=`awk 'BEGIN{RS=""}NF{$1=$1;print RS$0}' ./reports/${device_serialname}_memN.log`
                mem_D=`awk 'BEGIN{RS=""}NF{$1=$1;print RS$0}' ./reports/${device_serialname}_memD.log`
                echo "myChart.setDataArray([" $mem_N "], 'blue');" > ./reports/${device_serialname}_log_memN_tmp.log
                echo "myChart.setDataArray([" $mem_D "], 'green');" > ./reports/${device_serialname}_log_memD_tmp.log
                memN_tmp=`more ./reports/${device_serialname}_log_memN_tmp.log`
                memD_tmp=`more ./reports/${device_serialname}_log_memD_tmp.log`
                python merge.py $htmlname_mem ./reports/${device_serialname}_log_memN_tmp.log
                python merge.py $htmlname_mem ./reports/${device_serialname}_log_memD_tmp.log
                # sed -i '' '/new/a\  
                # '"$memN_tmp"'\
                # ' $htmlname_mem
                # sed -i '' '/new/a\  
                # '"$memD_tmp"'\
                # ' $htmlname_mem
            fi
        fi
        done
done
