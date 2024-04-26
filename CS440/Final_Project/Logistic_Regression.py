import statistics
import time

import numpy as np
from sklearn.linear_model import LogisticRegression


def run():
#     # Digit classification, each image is 28*28
#     # preprocess the validation data (not used as the accuracy rates were high enough)
#     val_labels = np.loadtxt('Final_Project/digitdata/traininglabels')
#     total = val_labels.shape[0]

#     val_images = []
#     with open('Final_Project/digitdata/trainingimages', 'r') as f:
#         for _ in range(total):  # Read a fixed number of images
#             # Read the image data (pixel values)
#             digit_data = []
#             pixel_map = {'#': 1, '+': 0.5, ' ': 0}  # map characters to pixel values
#             for i in range(28):
#                 row_data = list(f.readline().rstrip("\n"))
#                 row_data = [pixel_map[char] for char in row_data]  # convert characters to pixel values
#                 digit_data.append(row_data)
#             # Convert the pixel values to a numpy array and append to the list of images
#             digit_image = np.array(digit_data, dtype=np.uint8)
#             val_images.append(digit_image)

#     # Convert the list of images to a numpy array
#     val_images = np.array(val_images)

#     # Preprocess the digit testing data
#     test_labels = np.loadtxt('Final_Project/digitdata/testlabels')
#     total = test_labels.shape[0]

#     test_images = []
#     with open('Final_Project/digitdata/testimages', 'r') as f:
#         for _ in range(total):  # Read a fixed number of images
#             # Read the image data (pixel values)
#             digit_data = []
#             pixel_map = {'#': 1, '+': 0.5, ' ': 0}  # map characters to pixel values
#             for i in range(28):
#                 row_data = list(f.readline().rstrip("\n"))
#                 row_data = [pixel_map[char] for char in row_data]  # convert characters to pixel values
#                 digit_data.append(row_data)
#             # Convert the pixel values to a numpy array and append to the list of images
#             digit_image = np.array(digit_data, dtype=np.uint8)
#             test_images.append(digit_image)

#     # Convert the list of images to a numpy array
#     test_images = np.array(test_images)

#     times = 5
#     count = 0
#     data = [0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0]

#     for x in data:
#         lis = []
#         print("Sampling modifier: ", x)
#         # Record the start time
#         start_time = time.time()
#         train_labels = np.loadtxt('Final_Project/digitdata/traininglabels')

#         total = train_labels.shape[0]

#         train_images = []
#         with open('Final_Project/digitdata/trainingimages', 'r') as f:
#             for _ in range(total):  # Read a fixed number of images
#                 # Read the image data (pixel values)
#                 digit_data = []
#                 pixel_map = {'#': 1, '+': 0.5, ' ': 0}  # map characters to pixel values
#                 for i in range(28):
#                     row_data = list(f.readline().rstrip("\n"))
#                     row_data = [pixel_map[char] for char in row_data]  # convert characters to pixel values
#                     digit_data.append(row_data)
#                 # Convert the pixel values to a numpy array and append to the list of images
#                 digit_image = np.array(digit_data, dtype=np.uint8)
#                 train_images.append(digit_image)

#         # Convert the list of images to a numpy array
#         train_images = np.array(train_images)

#         # Reshape the images
#         # train_images = train_images.reshape((-1, 70*60))
#         test_images = test_images.reshape((-1, 28 * 28))
#         val_images = val_images.reshape((-1, 28 * 28))

#         train_image = train_images.reshape(5000, -1)
#         combined_array = np.concatenate((train_labels.reshape(-1, 1), train_image), axis=1)

#         for j in range(times):

#             size = int(total * x)
#             if size < total:
#                 sampled_indices = np.random.choice(total, size=size, replace=False)
#                 sampled_array = combined_array[sampled_indices]
#             else:
#                 sampled_array = combined_array

#             train_label = sampled_array[:, 0]
#             train_image = sampled_array[:, 1:]

#             # Define the model, it has default learning rate of 1.0
#             model = LogisticRegression(max_iter=1000)

#             # Train the model
#             model.fit(train_image, train_label)

#             # Evaluate the model
#             val_accuracy = model.score(val_images, val_labels)
#             # print(count, f" Val acc: {val_accuracy}")

#             # Test the model
#             test_accuracy = model.score(test_images, test_labels)
#             # print(count, f" Test acc: {test_accuracy}")

#             lis.append(test_accuracy)

#         # print(count, lis)

#         mean = sum(lis) / len(lis)
#         print("Mean accuracy for digit recognition: ", mean)

#         # Calculate the standard deviation of the list
#         std_dev = statistics.stdev(lis)
#         print("Standard deviation digit recognition:", std_dev)

#         # Record the end time
#         end_time = time.time()
#         elapsed_time = end_time - start_time

#         # Print the elapsed time
#         print("Time to run: {:.2f}".format(elapsed_time / times))
#         print()

#         count += 1


    # Face recognition each image is 70*60

    # Preprocess the facial validation data
    val_labels = np.loadtxt('Final_Project/facedata/facedatatrainlabels')

    total = val_labels.shape[0]

    val_images = []
    with open('Final_Project/facedata/facedatatrain', 'r') as f:
        for _ in range(total):
            # Read a fixed number of images
            # Read the image data (pixel values)
            digit_data = []
            pixel_map = {'#': 1, '+': 0.5, ' ': 0}  # map characters to pixel values
            for i in range(70):
                row_data = list(f.readline().rstrip("\n"))
                row_data = [pixel_map[char] for char in row_data]  # convert characters to pixel values
                digit_data.append(row_data)
            # Convert the pixel values to a numpy array and append to the list of images
            digit_image = np.array(digit_data, dtype=np.uint8)
            val_images.append(digit_image)

    # Convert the list of images to a numpy array
    val_images = np.array(val_images)

    # Preprocess the facial testing data
    test_labels = np.loadtxt('Final_Project/facedata/facedatatestlabels')
    total = test_labels.shape[0]

    test_images = []
    with open('Final_Project/facedata/facedatatest', 'r') as f:
        for _ in range(total):
            digit_data = []
            pixel_map = {'#': 1, '+': 0.5, ' ': 0}  # map characters to pixel values
            for i in range(70):
                row_data = list(f.readline().rstrip("\n"))
                row_data = [pixel_map[char] for char in row_data]  # convert characters to pixel values
                digit_data.append(row_data)
            # Convert the pixel values to a numpy array and append to the list of images
            digit_image = np.array(digit_data, dtype=np.uint8)
            test_images.append(digit_image)

    # Convert the list of images to a numpy array
    test_images = np.array(test_images)

    times = 5
    count = 0
    data = [0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0]

    for x in data:
        print("Sampling modifier: ", x)
        lis = []
        # Record the start time
        start_time = time.time()
        train_labels = np.loadtxt('Final_Project/facedata/facedatatrainlabels')

        total = train_labels.shape[0]

        train_images = []
        with open('Final_Project/facedata/facedatatrain', 'r') as f:
            for _ in range(total):
                digit_data = []
                pixel_map = {'#': 1, '+': 0.5, ' ': 0}
                for i in range(70):
                    row_data = list(f.readline().rstrip("\n"))
                    row_data = [pixel_map[char] for char in row_data]  # convert characters to pixel values
                    digit_data.append(row_data)
                # Convert the pixel values to a numpy array and append to the list of images
                digit_image = np.array(digit_data, dtype=np.uint8)
                train_images.append(digit_image)

        # Convert the list of images to a numpy array
        train_images = np.array(train_images)

        test_images = test_images.reshape((-1, 70 * 60))
        val_images = val_images.reshape((-1, 70 * 60))

        train_image = train_images.reshape(451, -1)
        combined_array = np.concatenate((train_labels.reshape(-1, 1), train_image), axis=1)

        for j in range(times):
            size = int(total * x)
            if size < total:
                sampled_indices = np.random.choice(total, size=size, replace=False)
                sampled_array = combined_array[sampled_indices]
            else:
                sampled_array = combined_array

            train_label = sampled_array[:, 0]
            train_image = sampled_array[:, 1:]

            # Define the model, it has default learning rate of 1.0
            model = LogisticRegression(max_iter=1000)

            # Train the model
            model.fit(train_image, train_label)

            # Test the model
            test_accuracy = model.score(test_images, test_labels)

            lis.append(test_accuracy)

        mean = sum(lis) / len(lis)
        print("Mean accuracy for facial recognition: ", mean)

        # Calculate the standard deviation of the list
        std_dev = statistics.stdev(lis)
        print("Standard deviation facial recognition:", std_dev)

        # Record the end time
        end_time = time.time()
        elapsed_time = end_time - start_time

        # Print the elapsed time
        print("Time to run: {:.2f}".format(elapsed_time / times))
        print()

        count += 1
        
run()